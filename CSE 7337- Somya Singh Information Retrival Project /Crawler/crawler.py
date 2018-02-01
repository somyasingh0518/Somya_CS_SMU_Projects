import requests
from bs4 import BeautifulSoup
import urllib.parse
import urllib.robotparser
import os.path
import sys
import time
import datetime
import math

# TO get the user input and Intializing
url = sys.argv[1]  # This is the starting URL
iterate = int(sys.argv[2]) # This is the number of pages usr wants to see
depth_to_go = int(sys.argv[3])  # Page depth to go up to
directory = sys.argv[4]  # directory name where the output is saved
AGENT_NAME = 'SomyaBOTCSE7337' # Defining Agent Name for the crawler
SHINGLE_SIZE = 5 # Defining the size for shingling

# If the user does not give URL starting with http
if not url.startswith("http"):
    url = "http://" + url

# This is to read robot.txt and make desion on Delay time if any and check if page is allowed or disallowed
def read_robot(inputurl):
    URL_BASE = 'http://lyle.smu.edu/~fmoore/'
    parser = urllib.robotparser.RobotFileParser()
    parser.set_url(urllib.parse.urljoin(URL_BASE, 'robots.txt'))
    parser.read()
    CheckDelay = parser.crawl_delay(AGENT_NAME)
    AllowedDisallowed = '{!r:>6}'.format(parser.can_fetch(AGENT_NAME, inputurl), inputurl)
    AllowedDisallowed = AllowedDisallowed.strip(' ')
    return (AllowedDisallowed,CheckDelay)

# This is to read the response code.
# This will decide to handle 4x and 5x code
def processing_response_code(ResponseString):
    #print (ResponseString)
    BrokenResponseCode = [404,500] # Informing Bot that Page is broken
    KillResponseCode = [401,403,503,504] # Informing Bot that Site may not bea ccepting the request so need to stop sending requests

    if ResponseString == 200:
        AllowDownload = 0
    if ResponseString in BrokenResponseCode:
        AllowDownload = 1
    if ResponseString in KillResponseCode:
        AllowDownload = 2
    if ResponseString == 301:
        AllowDownload = 3

    return AllowDownload

# This is to get shingles value
def get_shingles(f, size):
    shingles = set()
    buf = f.read() # read entire file
    for i in range(0, len(buf)-size+1):
        yield buf[i:i+size]

# This is to get jaccard coefficient
def jaccard(set1, set2):
    x = len(set1.intersection(set2))
    y = len(set1.union(set2))
    return x / float(y)

# This is to look for Duplicate Files
def check_for_duplicate(FileCheck):
    for FileName in os.listdir():
        if FileCheck != FileName:
            with open(FileCheck) as f1, open(FileName) as f2:
                shingles1 = set(get_shingles(f1, size=SHINGLE_SIZE))
                shingles2 = set(get_shingles(f2, size=SHINGLE_SIZE))
            DuplicateIndex = jaccard(shingles1, shingles2)
            if DuplicateIndex > 0.8:
                print ("The File is near duplicate to %s and is not been download" %(FileName) )
                return 1
    return 0

# The Crawling function
def trade_spider(max_pages):  # function(maximum number of pages to call variable)
    page = 1
    beginTime = datetime.datetime.now()
    urls = [url] # Array for URLS
    visited = [url] # Array of Visited URLS
    imageurl = [] # Array of Image URL
    pdfurl = [] # Array of PDF URL
    SitesVisited = [] # Array of all sites vistited
    BrokenLink = [] # List of all broken link
    DuplicateCount = 0
    DuplicateURL = [] # List of Duplicate Pages
    title_number = 0  # This to calculate title file
    OutgoingLinks = [] # This is to get list of all links outside /~fmoore/

    if not os.path.isdir(directory):  # To Check if Directory exists
        os.mkdir(directory)  # if it doesnt then make it

    os.chdir(directory)  # then change directory to that folder

    dsize = 0  # makes the amount of depth already crawled 0
    depth = [dsize] # To keep track of Page Depth
    # this checks if pg < max pg, the depth is < depth_to_go, and that urls are still available
    while page <= max_pages and depth_to_go >= dsize and len(urls) > 0:
        ManipulatedURL = manipulation_of_url(urls[0]) # Parsing the URL for reading robot txt
        AllowStatus,DelayTime = read_robot(ManipulatedURL) # Reading robot to check if it URL is ALLOWED vs DISALLOWED
        print ("=================================")
        # Reading Robot file and if no Crawl-Delay is define Its set to 2 sec between every request
        if DelayTime == None:
            time.sleep(2)
        else:
            time.sleep(DelayTime)

        # This is to make sure the URL is Allowed by robot txt file
        if AllowStatus in ('True','true'):
            SitesVisited.append(urls[0]) #Appending the url to URL visited
            try:
                try:
                    source_code = requests.get(urls[0], headers={'User-Agent':AGENT_NAME},allow_redirects=False)  # Calling URL with User-Agent defined
                    StatusCodeCheck = processing_response_code(source_code.status_code) # Reading the Response Code

                    # To handle 301 Response Code
                    if StatusCodeCheck == 3:
                        RedirectedLocation = source_code.headers['Location']
                        print("There is a Redirect Response Code")

                    # To handle 401/403/5xx Response Code
                    if StatusCodeCheck == 2:
                        print("The WebServer sent me a 401/403/5xx Response Code So I am Stopping the crawler")
                        sys.exit()

                    # To handle 404/500 Response Code
                    if StatusCodeCheck == 1:
                        print("The Page is Broken")

                    # To handle 2xx Response Code
                    if StatusCodeCheck == 0:
                        html = source_code.text  # get source code of page
                        soup = BeautifulSoup(html, 'html.parser')  # variable to call beautifulsoup(variable of the source code)
                        #print (soup)
                except:
                    print(urls[0])

                if StatusCodeCheck == 0:
                    try:
                        # To Format the Title in order to create name for the text file
                        name = soup.title.string  # removes all the uncessary things from title
                        name = name.replace("\n", "")
                        name = name.replace("\r", "")
                        name = name.replace("\t", "")
                        name = name.replace("|", "")
                        name = name.replace(":", "")
                        name = name.replace("/", "\\")
                        name = name.strip(' ')
                    except:
                        name = "Page With No Title " + str(title_number)  # if not title provided give a no title with number title
                        title_number += 1

                    num = 1
                    print ("The Title of Page is: " + name)
                    print ("The URL of Page is: " + urls[0])
                    name = '{0}.txt'.format(name)  # adds the .txt to the end of the name
                    print ("The Page is Download to File: " + name)
                    print ("=================================")
                    dupIndicator = 0
                    try:
                        if not os.path.isfile(name): #If File does not exist in the current directory
                            fo = open(name,'w') #Open the file
                            fo.write('<page_url href=\"' + urls[0] + '\"></page_url>\n' + html) # Write the source with first line as the html of the page
                            fo.close()

                            size = os.stat(name)
                            size = size.st_size
                            # Check for Duplication
                            dupIndicator = check_for_duplicate(name)

                            if ((dupIndicator == 1) or (size == 0)):
                                DuplicateCount += 1
                                DuplicateURL.append(urls[0])
                                os.remove(name)
                        else: # If File Name Already exists
                            new_name = name[:name.find(".")]
                            new_name = name + "_" + str(num) + ".txt"
                            fo = open(new_name, "w")
                            fo.write('<page_url href=\"' + urls[0] + '\"></page_url>\n' + html)
                            fo.close()

                            # Check for Duplication
                            dupIndicator = check_for_duplicate(new_name)

                            if ((dupIndicator == 1) or (size == 0)):
                                DuplicateCount += 1
                                DuplicateURL.append(urls[0])
                                os.remove(new_name)

                        #print(urls[0])
                        # Listing all outgoing links
                        # The URL is added to url and visited url
                        print ("*********************************")
                        print ("The Outgoing Link/s Present in the current Page is/are")
                        print ("*********************************")
                        for link in soup.findAll('a', href=True):
                            link['href'] = urllib.parse.urljoin(urls[0], link['href'])
                            print (link['href'])
                            if link['href'] not in visited:  # if the link is not in visited then it appends it to urls and visited
                                if 'pptx' not in link['href'] and 'mailto' not in link['href'] and '/~fmoore/' in link['href'] and '.pdf' not in link['href'] and '.jpg' not in link['href'] and '.gif' not in link['href'] and '.png' not in link['href'] and '.xlsx' not in link['href']:#makes sure no jpg or pdfs pass
                                    urls.append(link['href'])
                                    visited.append(link['href'])
                                if ('.jpg' in link['href']) or ('.gif' in link['href'] and (link['href'] not in imageurl)):#makes sure no jpg or pdfs pass
                                    imageurl.append(link['href'])
                                if (('.pdf' in link['href'])and (link['href'] not in pdfurl)):#makes sure no jpg or pdfs pass
                                    pdfurl.append(link['href'])
                                if '/~fmoore/' not in link['href']:
                                    OutgoingLinks.append(link['href'])
                        print ("*********************************")

                    except:
                        print("Can not encode file: " + urls[0])
                elif StatusCodeCheck == 3:
                    print("This is a Redirect page (Response code is 301): " + RedirectedLocation)
                    if RedirectedLocation not in visited and '/~fmoore/' in RedirectedLocation:
                        urls.append(RedirectedLocation)
                        visited.append(RedirectedLocation)
                else:
                    # This is to handle broken Pages
                    print ("=================================")
                    print("This is a Broken page (Response code is 404/500): " + urls[0])
                    BrokenLink.append(urls[0])
            except:
                print("Error: Encoding")
            #print("Depth:", dsize) # Total Depth
            print("Page Visited:", page, "pages")
            urls.pop(0) # remove the URL
            finalpage_count = page

            if page >= depth[dsize]:
                depth.append(len(visited))
                if StatusCodeCheck != 3:
                    dsize += 1

            if StatusCodeCheck != 3:
                page += 1
            # prints the amount of data collected in KB
            size_of_directory = get_tree_size(os.curdir) #/ 1000000000
            print(round(size_of_directory, 5), "KB")
            print ("*********************************")
            print ("\n")
        else: # TO handle DISALLOWED page
            print ("=================================")
            print ("This page is out of bound as requested by robot.txt file: " + urls[0])
            #print("Depth:", dsize)
            print("Page Visited:", page, "pages")
            urls.pop(0)
            print ("\n")

    OutgoingLinks = remove_duplicates(OutgoingLinks)

    print ("The Status of Execution is as below:")
    print ("=================================")
    print ("Total Execution time: " + str((datetime.datetime.now() - beginTime).total_seconds()) + " seconds")
    print ("=================================")
    print ("The total Page Visited are: ", finalpage_count)
    print ("=================================")
    print ("The List of All Visited URL:")
    print ('\n'.join(SitesVisited))
    print ("=================================")
    print ("The total Duplicate Pages are: ", DuplicateCount)
    print ("=================================")
    print ("The List of Duplicate Pages are:")
    print ('\n'.join(DuplicateURL))
    print ("=================================")
    print ("The List of All Graphic Files on the Site:")
    print ('\n'.join(imageurl))
    print ("=================================")
    print ("The List of All PDF Files on the Site:")
    print ('\n'.join(pdfurl))
    print ("=================================")
    print ("The List of All Broken URL:")
    print ('\n'.join(BrokenLink))
    print ("=================================")
    print ("The List of All Downloaded File:")
    print ('\n'.join(os.listdir()))
    print ("=================================")
    print ("The List of URL outside fmoore directory:")
    print ('\n'.join(OutgoingLinks))
    print ("=================================")

#In order for robot txt file to work it is usign this function as the robot is not in standard root space
def manipulation_of_url(url):
    parsed = urllib.parse.urlparse(url)
    FilePath = parsed.path
    NewFilePath = FilePath.replace("/~fmoore","")
    return NewFilePath

def remove_duplicates(OutgoingLinks):
    output = []
    seen = set()
    for value in OutgoingLinks:
        if value not in seen:
            output.append(value)
            seen.add(value)
    return output

def get_tree_size(path):
    """Return total size of files in given path and subdirs."""
    total = 0
    for entry in os.scandir(path):
        if entry.is_dir(follow_symlinks=False):
            total += get_tree_size(entry.path)
        else:
            total += entry.stat(follow_symlinks=False).st_size
    return total


trade_spider(iterate)

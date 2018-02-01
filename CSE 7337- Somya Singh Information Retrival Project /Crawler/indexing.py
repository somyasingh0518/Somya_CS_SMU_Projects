import os
import os.path

from bs4 import BeautifulSoup
from whoosh.fields import *
from whoosh.index import create_in
from whoosh.analysis import StemmingAnalyzer
from whoosh.analysis import StopFilter

# Get the folder to Index
folder_to_index = sys.argv[1]

# Name of the Directory where the Index is stored
dirname = "IndexDirectory"

# Reading Stop Word list
files = open("StopWordlist.txt","r")
lists = files.readlines()
StopWordList =[]

for i in range(len(lists)):
    StopWordList.append(lists[i].rstrip('\n'))

#print ("Stop Word List used are: ", StopWordList)
# Analyzer Defination for Stemming Analyzer with StopWord List
analyzer = StemmingAnalyzer(stoplist=StopWordList)
analyzer.cachesize = -1 # Unbounded caching, with memory performance

if not folder_to_index.startswith("/"):
    folder_to_index = "/" + folder_to_index

if not os.path.isdir(os.getcwd()+folder_to_index):
    print(folder_to_index[1:] + " does not exist.")
    exit(1)

#Defination of schema
# Schema = [DocumentID: Unique Number
#           Path: The URL for the Page
#           title: The Title of the Page
#           content: The Content of the page after stemming and eleminating]
schema = Schema(
    DocumentID=NUMERIC(stored=True, unique=True),
    path=NGRAM(minsize=4, maxsize=11, stored=True, sortable=True),  # ID: indexes the entire field as a single unit
    title=TEXT(field_boost=1.0, stored=True, phrase=True, sortable=True),
    #title=TEXT(stored=True, phrase=True, sortable=True),
    content=TEXT(analyzer=analyzer, stored=True, phrase=True, sortable=True)
)

if not os.path.exists(dirname):
    os.mkdir(dirname)

ix = create_in(dirname, schema)
writer = ix.writer()
path = os.getcwd()
path += folder_to_index
number_indexed = 1

for filename in os.listdir(path):
    print_filename = str(filename.encode('utf-8'))
    if print_filename[0:2] == "b\'" and print_filename.endswith("\'"):
        print_filename = print_filename[2:-1]

    print("indexing: "+print_filename)
    print("indexed number: "+str(number_indexed))
    #print (path)
    #print (filename)
    f = open(path + "//" + filename, 'r')
    content = f.read()

    soup = BeautifulSoup(content, 'html.parser')  # variable to call beautifulsoup(variable of the source code)
    link = soup.find('page_url', href=True) #special page_url tag created to store the url of page

    for script in soup.find_all('script'):
        script.extract()
    for style in soup.find_all('style'):
        style.extract()

    text = soup.get_text()
    # break into lines and remove leading and trailing space on each
    lines = (line.strip() for line in text.splitlines())
    # break multi-headlines into a line each
    chunks = (phrase.strip() for line in lines for phrase in line.split("  "))
    # drop blank lines
    text = '\n'.join(chunk for chunk in chunks if chunk)
    final_text = text#.encode('utf-8')
    #print(final_text)
    # Storing the document
    try:
        writer.add_document(DocumentID=number_indexed, path=link['href'], title=filename[:-4], content=final_text)
    except:
        writer.add_document(DocumentID=number_indexed, path=u'None', title=filename[:-4], content=final_text)

    number_indexed += 1

print("Committing please wait...")
writer.commit()
print("Finished")

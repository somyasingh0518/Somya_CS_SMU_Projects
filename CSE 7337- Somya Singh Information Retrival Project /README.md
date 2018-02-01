Somya Singh CSE 7337 Information Retrieval and Web Search - Term Project
        AUTHOR: Somya Singh
        Student ID: 47304053
        Email: somays@smu.edu
        CSE 7337 Spring 2017


PRE-REQUISITES:
  1. The program is developed on Mac OS 10.12.3
  2. The program uses python3 and its supporting packages

SOFTWARE NEEDS:
  1. If you are using Mac OS the default python version is 2.7. In order to install python3 please follow below steps
      1. Install Homebrew "ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)""
      2. Now install python3 "brew install python3"
      3. To check python version you have to use "python3 -V". The program is supporting any version of 3.x
  2. In Order to run this program we need few python3 packages that you will need to install. The packages are listed below with    installation instruction and official documentation page sites
      1. Package "requests" - "pip3 install requests" (https://pypi.python.org/pypi/requests/2.13.0)
      2. Package "beautifulsoup4" - "pip3 install beautifulsoup4" (https://pypi.python.org/pypi/beautifulsoup4)
      3. Package "whoosh" - "pip3 install whoosh" (https://pypi.python.org/pypi/Whoosh/2.7.4)

FILE/FOLDER STRUCTURE:
  Root folder
    - Crawler folder
    - README.md

  Crawler folder
    - crawler.py: This is python script which is going to crawl through URL and download into folder given by the user (OutPutFolder)
    - indexing.py: This is python script which read the folder (OutPutFolder) to do stemming, stop word elimination and allocate a document ID and save it in a cache folder name (IndexDirectory)
    - search.py: This is python script which based on IndexDirectory will help user to search for the word/terms.
    - outputverification.py: This script is to output two things
            - Index Schema which shows saved output
            - Results for Top 20 words with its document frequency
    - StopWordlist.txt: Contain list of stop words used
    - IndexDirectory: This is the Index directory created by indexing.py which contain the index data with document id.
    - OutPutFolder: The folder contains all the downloaded pages in txt format

HOW TO EXECUTE:
  1. Navigate to Crawler folder
  2. The first step will be to execute crawler.py. The input for the program are
        a. Starting URL
        b. Total Number of Pages to see
        c. Total Page Depth you want to see (Starting URL is Page Depth 0)
        d. The folder where you want to download the content (OutPutFolder).
     So if you want to see 30 pages up to a depth of 10 you will need to run following command on your terminal:
        python3 crawler.py http://lyle.smu.edu/~fmoore 30 10 OutPutFolder
  3. The script will execute and create a folder "OutPutFolder" which will contain downloaded pages in text format.
  4. Now please execute indexing.py. The input for the program is the folder (OutPutFolder). You will run below command in terminal
      "python3 indexing.py OutPutFolder"
  5. This should create a folder name "IndexDirectory"
  6. In order to verify Stored information and answer to question number 7 of the project please execute  "python3 outputverification.py"
  ***End of First Phase***
  ***Start of Second Phase***
  7. This phase of project uses the first phase IndexDirectory.
  8. In order to do search execute search.py by typing below command
      python3 search.py
     The search script will first display the number of words that we have stored in our Dictionary. Once the display is done it will ask for the user to enter search term. The script does the case in-sensitive search which means user can input in any case he/she wish too.
     The script will keep running unless user input the word "STOP" (case in-sensitive).

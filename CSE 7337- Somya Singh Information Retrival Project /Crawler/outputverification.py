import sys
from whoosh.index import open_dir
from whoosh.query import Every

#Reading the Directory
dirname = "IndexDirectory"
ix = open_dir(dirname)
tr =ix.reader() #Reading the Index


# To Calculate the frequency of top 20 most frequent words
for freq, word in tr.most_frequent_terms('content', number=20):
    print ("%s appears %d times and Document Frequency is %s" % (word, freq, tr.doc_frequency('content',word)))

print ("=====================")

# To Print the Index
results = ix.searcher().search(Every('content'),limit=100) #To get all the Content
for result in results:
    #print ("Rank: %s DocumentID: %s Content: %s Path: %s Title: %s" % (result.rank, result['DocumentID'], result['content'], result['path'], result['title']))
    print ("DocumentID: %s Path: %s Title: %s" % (result['DocumentID'], result['path'], result['title']))
    #print ("Content:")
    #print (len(result['content']))
    #print ("=====================")

print ("=====================")

list_of_words = 0
for result in results:
    list_of_words = list_of_words + len(result['content'])

print ("The total Number of Words in Dictionary are: %d" % (list_of_words)) # Answer to 2b

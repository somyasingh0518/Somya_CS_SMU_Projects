import sys

from whoosh import highlight
from whoosh import qparser
from whoosh.index import open_dir
from whoosh.scoring import TF_IDF
from whoosh.query import Every

search_type = "TFIDF"
operation_type = "OR"
dirname = "IndexDirectory"

ix = open_dir(dirname)

def CountWords():
    sinput =""
    #ix = open_dir(dirname)
    qp_2 = qparser.MultifieldParser(['content'], ix.schema,group=qparser.OrGroup)
    qp_2.add_plugin(qparser.PlusMinusPlugin)
    query2 = qp_2.parse(sinput)
    TotalWords = 0

    with ix.searcher(weighting=TF_IDF()) as searcher:
        results = searcher.search(query2,terms=True, limit=500)
        #print(list(searcher.lexicon("content")))
        #print(len(list(searcher.lexicon("content"))))
        TotalWords = TotalWords + len(list(searcher.lexicon("content")))
    print("********************************************************************")
    print ("The total Number of Words in Dictionary are (not including URL): %d" % (TotalWords)) # Answer to 2b
    print("********************************************************************")


def SearchAlgo(searchkey,loop):
    search_input = searchkey
    op_type = qparser.OrGroup
    w = TF_IDF()
    TotalCount = loop

    #ix = open_dir(dirname)

    qp = qparser.MultifieldParser(['DocumentID','content'], ix.schema,group=op_type)
    qp1 = qparser.MultifieldParser(['DocumentID','title'], ix.schema,group=op_type)

    qp.add_plugin(qparser.PlusMinusPlugin)
    qp1.add_plugin(qparser.PlusMinusPlugin)

    query = qp.parse(search_input)
    query1 = qp1.parse(search_input)

    with ix.searcher(weighting=w) as searcher:
        results = searcher.search(query, terms=True, limit=6)
        results1 = searcher.search(query1, terms=True, limit=6)

        results.fragmenter = highlight.ContextFragmenter(surround=100)
        results1.fragmenter = highlight.ContextFragmenter(surround=100 )

        found_doc_num = results.scored_length()
        found_doc_num1 = results1.scored_length()


        if ((found_doc_num < 3) and (TotalCount==0)):
            My_Dictionary(search_input,found_doc_num)
        else:
            run_time = results.runtime
            if found_doc_num == 0:
                 final_top_output = "Sorry 0 Search Results found.\n" \
                     "Search Results for \""+search_input+"\" using "+search_type+" (" + str(run_time) + " seconds)\n"

            else:
                 final_top_output = "Top " + str(found_doc_num) + " Search Results\n" \
                     "Search Results for \""+search_input+"\" using "+search_type +" Ranking and "+ operation_type+\
                                    " operation to score (" + str(run_time) + " seconds)"

            print(final_top_output)

            if results:
                print("=============================")
                print("===========Results===========")
                print("=============================")
                i = 1
                for hit in results:
                    snip = hit.highlights('content', top=5)
                    path = hit['path']
                    title = hit['title']
                    DocumentID = hit['DocumentID']
                    increementtitle = 0 ;

                    for hit1 in results1:
                        DocumentID1 = hit1['DocumentID']
                        if DocumentID == DocumentID1:
                            increementtitle=1;
                            break
                        else:
                            increementtitle=0;

                    if increementtitle == 1:
                       score = hit.score + 0.5
                    else:
                       score = hit.score

                    print("Result %d" %(i))
                    i = i + 1
                    print("**********************")
                    print("Path: %s" %(path))
                    print("Title: %s" %(title))
                    print("**********************")
                    print(snip, "\n")
                    print("**********************")
                    print("Score: %s" %(score))
                    print("============================")
                    print("============================")


def My_Dictionary(inputsearch,results):
    finalstring = inputsearch
    input_s = inputsearch.split()
    the_dictionary={"word":[" alternates"],"beautiful":[" nice"," fancy"],"chapter":[" chpt"],"responsible":[" owner"," accountable"],"freemanmoore":[" freeman"," moore"],"dept":[" department"],"brown":[" beige"," tan"," auburn"],"tues":[" Tuesday"],"sole":[" owner"," single"," shoe"," boot"],"homework":[" hmwk"," home"," work"],"novel":[" book"," unique"],"computer":[" cse"],"story":[" novel"," book"],"hocuspocus":[" magic"," abracadabra"],"thisworks":[" this"," work"]}

    for word in input_s:
        if word in the_dictionary:
            for searchstring in the_dictionary.get(word):
                finalstring = finalstring + searchstring

    SearchAlgo(finalstring,1)


def SearchLoop():
    s_string = ' '
    while True:
        s_string = input("Please Enter The Search Term (Enter stop to STOP the search): ")
        print("============================")
        if (s_string.lower() != 'stop'):
            SearchAlgo(s_string,0)
        else:
            break


CountWords()
SearchLoop()

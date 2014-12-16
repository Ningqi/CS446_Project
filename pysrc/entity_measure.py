import nltk.data
from nltk.stem.porter import *
from nltk.collocations import *
from nltk.tokenize import *
from nltk.corpus import wordnet as wn

def get_stop_lst():
        stop_lst = []
        with open("data/stop_words.txt", 'r') as fileobj:
                for line in fileobj:
                        if len(line) > 0:
                                stop_lst.append(line[:len(line) - 1])
        return stop_lst

def main():
	data = "data/top_100_entities.txt"
	pathToData = "data/funnyReviews/"
	fileName = "rev_data_"
	suffix = ".txt"

	lines = []
	with open(data, 'r') as f:
		lines = f.readlines()
	
	entity_weight = []
	wn_entities = []	
	for line in lines:
		key =  line.split(",")[0].split(":")[1].strip()
		value = line.split(",")[1].split(":")[1].strip()
		#print wn.synsets(key, pos=wn.NOUN)
		wn_entities.append(wn.synsets(key, pos=wn.NOUN)[0])
		entity_weight.append( (key, value) )
	
	tokenizer = nltk.data.load('tokenizers/punkt/english.pickle')
	tokenizer2 = RegexpTokenizer(r'(\w|\')+')
	stemmer = PorterStemmer()
	tokenize = tokenizer2.tokenize
	stem = stemmer.stem
	stop_lst = get_stop_lst()	
	punctuation = set(['.',',','?','!','\'','\"','`','``','*','-','/','+'])

	review = ""
	with open(pathToData + fileName + str(1) + suffix, 'r') as f:
		review = f.read()


	review_lines = tokenizer.tokenize(review.lower())
	scores = []
	for sentence in review_lines:
		tokens = tokenize(sentence)
		clean_line = [stem(token) for token in tokens if token not in stop_lst and token not in punctuation and token.isalpha()]
		#print clean_line
		for item in clean_line:
			word1 = wn.synsets(item, pos=wn.NOUN)
			if len(word1) > 0:
				if word1[0] in wn_entities:
					print word1[0]
		

	# score = ( score + score_sentence_tag(clean_line, tag, cooccurances, probs) + 1) / (1.0*len(clean_line) + 1.0)
                        			
	
	return 0


if __name__ == "__main__":
	main()

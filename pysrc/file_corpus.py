
def main():
	datafile = "data/funny_reviews_5.txt"
	pathToOutput = "data/funnyReviews/"
	outputName = "rev_data_"
	lines = []
	with open(datafile, 'r') as f:	
		lines = f.readlines()
	
	for i, line in enumerate(lines):
		cleanOutputRev =pathToOutput+outputName+str(i)+".txt"
		with open(cleanOutputRev, "w") as op:
			clean_line = line.replace("\\n", "\n")
			op.write(clean_line)

if __name__ == "__main__":
	main()

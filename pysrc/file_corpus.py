
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


def move_processed():
	pathToData = "data/temp.output"
	pathToEnts = "data/proccessed.output"
	pathToMove = "data/toMoveFiles.output"
	all_lines = []
	with open( pathToData, 'r') as f:
		all_lines = f.readlines()
	
	remove_lines = []
	with open( pathToEnts, 'r') as f:
		for line in f:
			fline = line.split('.')
			remove_lines.append(fline[0] + "." +  fline[1]+'\n')

	remaining = set(all_lines) - set (remove_lines)
	lines = set(all_lines) - remaining

	with open( pathToMove, 'w') as f:
		f.writelines(lines)

if __name__ == "__main__":
	move_processed()

CS446_Project
=============


To generate the data, first move the yelp data to the data folder. Then run ParseYelp.java, this will extract all of the funny reviews and store them in funny_reviews.txt and non-funny reviews and extract them in not_funny_reviews.txt.

In order to be able to run the script you need a training data and testing data. I implemented a quick script that does samples this data to generate a training and testing. To do this you need to run sampleData.py with a file, which will output two files, one output_sample.txt which contains 10% of the original file and output_cleanedtxt which contains the other 90%.

To run use the following command 
./scripts/compileLBJ lbj/ReviewsClassifier.lbj

if you want to run training again, remove the directory under lbjsrc.

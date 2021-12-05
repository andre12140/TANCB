# TANCB
## Tree augmented naive Bayes Classifier

Classification is the problem of categorizing unseen examples into predefined classes based on a set of training examples. The learning algorithm generates a model from a complete set of independent instances and their corresponding categories; the model is then used to predict the categories of novel instances. 

With the continuous expansion of data availability it is critical to provide automatic classification and analysis from raw data to support decision-making processes. Domains of application include surveillance, security, internet, finance, health care, just to name a few. 

There are several approaches to perform classification, namely those based on neural networks, support vector machines, Bayesian network classifiers, regression, among others. In this project we focus our attention on discrete Bayesian network classifiers.

### To run the program:

java -jar POOProject.jar "train" "test" "score"

where train and test are the names/paths of the input files, and score is the string that identifies the score to be used to build the TAN model (LL or MDL).

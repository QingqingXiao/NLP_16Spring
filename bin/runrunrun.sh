java nlp/nyu/FeatureBuilder ../data/WSJ_02-21.pos-chunk ../data/training_feature.txt training
java nlp/nyu/FeatureBuilder ../data/WSJ_24.pos ../data/test_feature.txt test

java -cp "../*:." -Xmx3600m nlp/nyu/MEtrain ../data/training_feature.txt ../data/maxent.model
java -cp "../*:." nlp/nyu/MEtag ../data/test_feature.txt ../data/maxent.model ../data/response.chunk

cd ..
python chunk-scorer.py
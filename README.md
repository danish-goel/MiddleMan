# MiddleMan
# Team - Triad
# Submission for Hack InOut 2016
# Closest Track - "Build your Bot with Advance Al engine"

## Idea
While we saw most of the participants had jumped into coding chat bots usig standard APIs, we took this hackathon as an opportunity to experiment and hack into something new a bot could possibly do, besides just finding keywords in your questions to come up with answers. We realized that instead of just reading our text, **could the bot possible read our face as well**? 

The idea we came up with was 

This lead us to finding some interesting use-cases of a bot which could **see us text** and possibly make some of it's own decisions a tad bit smarter - 

 - One usecase would be more meaning text predictions
 - Another possible usecase
 - It could also be use for

While the idea was just an idea in this hackathon and we had a whole bunch of incentives to use some sponsor APIs, we went with experimenting with the first use case for this hackathon as it's implementation

## Implementation

Being a text completion bot, the application should be as real-time as possible. While we used a REST API for getting autocomplete words and their sentiments, we did not want to add an overload of having the vision component too being done over REST. APIs such as [] and [] though provide some good APIs, they unfortunately are extremely poor to use in this context of sentiment analysis of a person's face in real time. The human face gives tiny micro expression which collectively over a time period decide and we would eventually miss out on a lot of analysis time and data if we went in a REST API for such a task. Hence, we decided to code up a quick algorithm ourselves. While deep learning algorithms and implentations claim for a superior accuracy, they take a huge time to train and the available trained models too run take a while (not real time) to generate an output, not to mention that they make the app bulky. Since we were dealing with a binary case here of classifying a "positive" and "non-positive" user sentiment, we went for a statistical machine learning approach of HAAR cascades to first identify the face and then identify the micro-expression. Given the time constraints we focussed on one micro-expression - **The Human Smile** which we extracted using a thresholding based templating method after isolating the mouth on the face and fittig Hough transform lines. The following images show the process of extracting the smile from the face. 

After obtaining the sentiment of the user we can move towards using the Haven API's to aid text completion and prune their response based on the user's sentiment using the HOD sentiment analysis API. After integrating both these components the app looks as follows:



## Snapshots/Demo

## Code/APIs/Dependencies


## Future Work and Challenges

 - An important thing to take care while implementing this bot was that it was supposed to **Real Time**. While our text completion API too was a REST API, we would have idealy have it to be on our device itself for future work in order to support a real time operation.
 - Forced landscape view. Even though texting is more predominantly done in the portrait setup, we work with landscape mode in order to support both views at the moment. The camera view can be completely removed to make the app shift to portrait mode and look just like your every day text messenger.
 - We look to explore more usecases on how a seeing chat bot can make more intelligent decisions. :)

## References

 - [1] Smile Mood Meter, MIT ()[]
 - [2] HOD Text Completion API ()[]
 - [3] HOD Sentiment Analysis API ()[]
 - [4] Google Cloud Vision ()[]
 - [5] Helix, PreCog Research Group ()[]
 - [6] OpenCV ()[]
 - [7] Parse ()[]
 - [8] 

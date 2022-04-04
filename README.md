# Bloom Notetaking App for CS 398
Bloom is an Android notetaking application that features note creation for everyday users as well as daily journalling in the form of Daily Entries. This application was created as a course project for CS 398 during the course's inaugural term in Winter 2022.

## Wiki
The wiki contains a [landing page for the project](https://git.uwaterloo.ca/a388shar/cs398-project/-/wikis/Home) plus detailed pages for different course deliverables:

1. [Introduction](https://git.uwaterloo.ca/a388shar/cs398-project/-/wikis/Home)
1. [Requirements](https://git.uwaterloo.ca/a388shar/cs398-project/-/wikis/Requirements)
1. [Project Plan](https://git.uwaterloo.ca/a388shar/cs398-project/-/wikis/Project-Plan)
1. [User Persona](https://git.uwaterloo.ca/a388shar/cs398-project/-/wikis/User-Persona)
1. [Analysis & Design](https://git.uwaterloo.ca/a388shar/cs398-project/-/wikis/Analysis-&-Design)
1. [Implementation](https://git.uwaterloo.ca/a388shar/cs398-project/-/wikis/Implementation)
1. [Testing](https://git.uwaterloo.ca/a388shar/cs398-project/-/wikis//Testing)
1. [Releases](https://git.uwaterloo.ca/a388shar/cs398-project/-/wikis/Releases)
1. [Profiler](https://git.uwaterloo.ca/a388shar/cs398-project/-/wikis/Profiler)

## Issues and Milestones
We used [Issues](https://git.uwaterloo.ca/a388shar/cs398-project/-/issues) to track the work to be done (includes features and bug fixes, tagged accordingly). Milestones for Sprint 1 through 3 can be found [here](https://git.uwaterloo.ca/a388shar/cs398-project/-/milestones)

## Releases
Here are the [apks](https://git.uwaterloo.ca/a388shar/cs398-project/-/wikis/Releases) from each sprint and the most recent update of the app.

## Reflection
While working on Bloom the team learned a lot during the 3 sprints. Below are our take aways after working on the app.

What went well:
* Using CodeWithMe
    * Ajay and Johnny used CodeWithMe in Intellij to setup the SpringBoot service. This allowed for pair programming even when the group was not physicially together. Many bugs were caught and the overall code ended up being cleaner having two people working together.
* Doing code reviews
    * Before a big feature was merged into any branch we tried to setup code reviews. A merge request would be opened before a branch was merged, allowing for other member to look at the changes. This was helpful in spotting certain bugs and having all members aware of the incoming changes that may impact their code.
* Evenly splitting up work
    * From sprint 1 we all agreed on how the work would be split up. Ajay and Johnny would work on backend changes and Lucas and Yolanda would work on the frontend. This made paired programming viable since we split up into teams of 2. Also, over the course of the 3 sprints we eventually become experts in our sections of the codebase and adding new features was easier overtime.
* Communication
    * We had a great team chemisty and this led to frequent communication. We setup a Facebook Messanger group chat where we communicated our status on features and asked for help when we needed it. There was never a time when a member was unresponsive or unwilling to help. 
* Clearly definded UI early
    * Before we started sprint 1 we made sure we had a basic UI that we all agreed on. This concept was made in Figma. While small changes were made along the way as we prioritize certain features the concept remained the same. This was useful as it allowed everyone on the team to have input into what the app looked like and it was not soley on the person incharge of the frontend.

What did not go well:
* It is hard to work on a feature synchronously
    * One issue that arised due to how we split up the work load was working on a feature synchronously. Sometimes the frontend would be be waiting on the backend to finish their work before they could begin making UI changes. Other times database changes could not be made until certain models were complete. Overall, there were many cases where one person was blocked by someone else work.
* Information was lost between meetings
    * While we would have productive and informative meetings a lot of the information was lost. This is because while in a meeting we would all be focused on the person talking and not taking any notes. This resulted in many follow up conversations due to lost information.

what we would do differently next time:
* Get everyone to work on multiple layers of the app
    * As mentioned before we struggled with working on a feature synchronously. One solution to this would be to assigned a single feature to a team member for them to complete. This would reduce one person work being blocked by someone else. Also, this allows for us all to have more experince with the different layers in the app.
* Take better notes during meetings
    * As mentioned above a lot of information was lost in meetings due to us not taking notes. If we were to start over we would prioritize taking notes during conversations to retain information for later.
* Setup contracts early
    * A change we made going into sprint 3 was to setup contracts. This is when we all get together and decide what a certain model should look like or what methods a class should have. This reduced the amount of times we were blocked by other peoples work as we knew what to expect when it was complete. If we would to do this project again we would make sure to setup contracts before coding.

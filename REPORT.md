## Description
<img src="/docs/RoomScreenshot.png" width="350">

The goal of Clean Student is that a user can keep track of the cleaning of his house together with his housemates.
In this Android app a user can set up a group by inviting his housemates by e-mail. Once a group is created rooms can be created and
can be assigned to members of the group. In these respective rooms a list of tasks can be created. Which can be marked on completion, so that groupmembers can see what is done and when.

## Technical Design

### High level overview
<img src="/docs/HighLevelOverview.png" width="500">

### Detailed overview

**Log In**

*LogInActivity*

In this activity a previously registered user can log in with email and password verification. If credentials are correct the user is send through to <i>MyGroupsActivity</i>. If a user has forgotten his or her password, the user can click on "Password forgotton?" which will redirect the user to <i>PasswordRecoveryActivity</i>. If a user wants to register a account it can click on "No account yet?" which will redirect the user to <i>RegisterActivity</i>.

*RegisterActivity*

In this Activity a user can register an account. By entering email, password, first name and last name, a user can get authentication to FireBase. Besides this a user is given a userid based on a hashkey of its email which is written in the database. A email confirmation is also send. 

*PasswordRecoveryActivity*

In this activity, a user can fill in a email. If this email is a previously registered email on FireBase, a password reset email is send to this email. In this mail the user can fill in a new password in case it was forgotten.

**Main functionality**

*MakeGroupActivity*

In this Activity a user can create a new group. He/She can give a name to a group and by clicking a floating action button a user can add members in a dialog. The user needs to fill in a email and nickname of a future member. This is then saved to a ArrayList which can be seen in a ListView. OnLongItem Clicking a member in the ListView the user can still edit/remove a future member in case a typo was made.
On Clicking save the group is created. This process contains several steps.
Firstly a unique groupid is created by hashing the email of the currently signed in user and adding a timestamp to it.
Then the groupid and name is saved under the current user in FireBase. The group is also written in FireBase under groups.

Secondly all invited members that were stored in a ArrayList need to get acces to Firebase as well as to the group. To achieve this,
all users are given authentication to FireBase, then these users are written to the DataBase (Again with hash of email as user id).
And then they are added to the group as members. And under their userid the group is also stored. Then every user receives an email with an invitation to the Clean Student. The email says that all they need to do is download the app and set up a password clicking a password reset mail. When the users have set a password they can log in and they will automatically see they are member of the group.

Below I will talk about how I achieved this and I will make this text clearer by showing my Database structure.

*MyGroupsActivity*

In this navigationdrawer activity a user can see a ListView which shows all groups this user is part of. Through navigation drawer
a user can go to his account details in <i>MyAccountActivity</i> or can sign out his account and go back to <i>LogInActivity</i>.
On click a floating action button a user can create a new group by being directed to <i>MakeGroupActivity</i>.
By LongItemClicking a group in the ListView a user can delete a group. Every member of a group can delete a group. On ItemClick on a group a user can see the contents of this group in <i>RoomActivity</i>.

*RoomActivity*

In this Activity a user can see a ListView with the rooms that have been added to a group. A room contains the name of the room as well as the groupmember responsible for it. On clicking the floating action button a room can be added in a dialog. In the dialog a name has to be filled in and a groupmember has to be selected from a spinner. On clicking add, the room is written to FireBase. On LongItemClick a room can be deleted. Again this can be done by all members in the group. on ItemClick the content of the room that has been clicked can be seen in TaskActivity. On clicking the return arrow a user can return to <i>MyGroupsActivity</i>.

*TaskActivity*

In This Activity a user can see a ListView with the tasks that have to be done in room. On Clicking the floating action button a new task can be added to FireBase. OnLongItemClick on a task in the ListView, the task can be edited or deleted. On clicking the Checkbox next to task a user can mark a task as completed. On Clicking a checkbox a date appears below the task to mark when the task was done.
At the moment all users can click all checkboxes. On clicking the return arrow a user can return to <i>RoomActivity</i>.

**My Account**

*MyAccountActivity*

Since in <i>MakeGroupActivity</i> a user can invite other users to a group and give them a name. I thought it would be nice to add the possibility for a newly invited user to see his or her account information. If a user has given another user a funny nickname but the user in question is not happy with this name. It can be changed by pressing the edit button. This will redirect to <i>EditMyAccountActivity</i>. This Activity is a NavigationDrawer Activity which means a user can go back to <i>MyGroupsActivity</i> and
also Sign out if he/she wishes to do so.

*EditMyAccountActivity*

In this activity a user can edit his/her account information. For now a user can only edit her name and lastname. In theory it is ofcourse possible to let a user change his or her email as well. But since I use the hash of an email as user id this would involve deleting a user, and then writing this user again with a new userid. On top of this the user should still have acces to the same groups. This would result in a lot of reading and writing and looping through the database.
Given the time the choice was not to do this.


## Challenges during development

**DataBase Structure**

I ran into some unexpected difficulties in choosing a right database structure. At first I refused to use objects since I did not really
feel comfortable with using objects yet and a lot of pushkeys. I felt it was necessary to use pushkeys because otherwise data in the Database would get overwritten alot. But by using pushkeys it was needed to loop a lot through the database to find the right data which seemed unneeded if keys could be send through next activities using intents. That is why I decided to use the following database structure. By using groupids and names of rooms and tasks as keys, navigation through database is efficient in the sense that it is known beforehand where data is stored instead of having the need to loop through the database. The downside is that the using of room and task names as keys can be prone to crashing the app if special symbols are used. Another downside is that by creating duplicate rooms or tasks these are overwritten in the database. But the analogy I followed is that studenthomes do not very often contain two bathrooms for example. A better database is thinkable however.

I also found out that in Firebase it is often better to keep your data unnested and as shallow as possible. This is because on a onDataChange call the Json file requested can be unnecessarily big then which would make database requests slow and which can cause asynchronous problems. The downside is that some data might be stored double in the database. According to FireBase documentation this is advised however.


<img src="/docs/DataBaseStructure.png" width="350">

**FireBase Asynchronousity**

*MakeGroupActivity*
This Activity was the biggest challenge of the project. I had the ambitious idea that on creating a group. I would automatically authenticate a user , register him/her to database, add him/her to the group and send them a invitation email. I thought it would be user friendly if a user had only to choose a password and log in and would find he/she would automatically have acces to the group.

However Firebase is asynchronous and the problem I faced directly was the following; I added all future members to an ArrayList. On creating group I was trying to loop through this arraylist member for member to authenticate, register, add to group and send them a mail one by one. However because the FireBase calls where happening on another thread, the for loop would finish before I had done all firebase actions. So I had to figure out a solution for this problem.

I found out on StackOverFlow about Future objects. With Future objects you can force a function to wait untill all the synchronous calls are done (for more info see code). This worked to some extent. The problem I had now was that I could authenticate all users in FireBase Auth and send them a email. But I could not write them to the database with their Firebase userid. When authentication is given to a user, there is small window of time where the uid can be reached, but it seemed to short to write the user to the database. I tried to build another Future around it. So that I would have a Future in a future but this did not work. One solution was to sign in every user and then write them into the database, because a userid can be accesed on user that is signed in. but this seemed a difficult and unwanted situation. Therefore I chose to use a hash of a user his mail, since a email is kind of unique and use this a user id in the Database. Like this if a user is signed in, his/her email can be retrieved and hashed to gain acces to the database.

A weakness is that a hashfucntion can return a similar hash for another email adress, but this flaw is accepter for now.

*ListView Issues*

Because FireBase is asynchronous it can sometimes be a pain in the bottom to make onDataChange requests directly visible in a ListView.
I found out FireBaseListAdapters can be used in Android which solve this automatically. I had to use object for this however and so I did. I feel I master objects now:).

**Time**

Initially the plan was to set a weakly deadline on tasks and have a scoreboard where groupmembers would gain point (or lose them) on not making their cleaning deadline. Although this could be implemented I had to spend to much time on fixing asynchronous problems. 
It is possible to set reminders and make them run in the background to send users push notifications when the deadline is closing in.

To make up for this lack of functionality I did make it possible to mark tasks as complete and set a date of completion below it.
In this way a group can still see if tasks have been done or not and they then punish or reward for it as they choose. A downside is that at the moment every user can check and uncheck a task in every room. Ideally you would want only the user responsible for his or her task to be able to check it. To avoid possible jokes of teammates.





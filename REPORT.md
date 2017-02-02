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

This Activity contains the 

*MyGroupsActivity*

In this navigationdrawer activity a user can see a ListView which shows all groups this user is part of. Through navigation drawer
a user can go to his account details in <i>MyAccountActivity</i> or can sign out his account and go back to <i>LogInActivity</i>.
On click a floating action button a user can create a new group by being directed to <i>MakeGroupActivity</i>.
By LongItemClicking a group in the ListView a user can delete a group. Every member of a group can delete a group, which might seem risky but I will explain my choice later on. On ItemClick on a group a user can see the contents of this group in <i>RoomActivity</i>.

*RoomActivity*

In this Activity a user can see a ListView with the rooms that have been added to a group. A room contains the name of the room as well as the groupmember responsible for it. On clicking the floating action button a room can be added in a dialog. In the dialog a name has to be filled in and a groupmember has to be selected from a spinner. On LongItemClick a room can be deleted. Again this can be done by all members in the group. on ItemClick the content of the room that has been clicked can be seen in TaskActivity. On clicking the return arrow a user can return to <i>MyGroupsActivity</i>.

*TaskActivity*

In This Activity a user can see a ListView with the tasks that have to be done in room. On Clicking the floating action button a new task can be added. OnLongItemClick on a task in the ListView, the task can be edited or deleted. On clicking the Checkbox next to task a user can mark a task as completed. On Clicking a checkbox a date appears below the task to mark when the task was done.
At the moment all users can click all checkboxes. On clicking the return arrow a user can return to <i>RoomActivity</i>.

**My Account**

*MyAccountActivity*

Since in <i>MakeGroupActivity</i> a user can invite other users to a group and give them a name. I thought it would be nice to add the possibility for a newly invited user to see his or her account information. If a user has given another user a funny nickname but the user in question is not happy with this name. It can be changed by pressing the edit button. This will redirect to <i>EditMyAccountActivity</i>. This Activity is a NavigationDrawer Activity which means a user can go back to <i>MyGroupsActivity</i> and
also Sign out if he/she wishes to do so.

*EditMyAccountActivity*

In this activity a user can edit his/her account information. For now a user can only edit her name and lastname. In theory it is ofcourse possible to let a user change his or her email as well. But since I use the hash of an email as user id this would involve deleting a user, and then writing this user again with a new userid. On top of this the user should still have acces to the same groups. This would result in a lot of reading and writing and looping through the database.
Given the time the choice was not to do this.


## Challenges during development

**FireBase Asynchronousity**

**DataBase Structure**

**Time**

## Defend decisions





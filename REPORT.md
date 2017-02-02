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

*TaskActivity*

**My Account**

*MyAccountActivity*

*EditMyAccountActivity*

## Challenges during development

**FireBase Asynchronousity**

**DataBase Structure**

**Time**

## Defend decisions





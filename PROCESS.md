#day1
Brainstorming and writing of Proposal

#day2
Brainstorming. Making DESIGN.md
Checken for feasibility of planned functionalities.

#day3
Getting to know the group.
Finished of DESIGN.md.
Started on Login Activities.

#day4
Worked on prototype. 
Coded login, registration and password recovery with Firebase. Added activation e-mail and password reset e-mail support.
Made all activities for prototype

#day5 
Presentation
Struggled with reading writing to database and its structure.
Did research into recyclerviews.

#day 6
Enjoyed my saturday

#day 7
Made Activites: InviteActivity, MakeGroupActivity, ProfileActivity
It is now possible to add people through name and email.
Spend a lot of time to push data from InviteActivity to MakeGroupActivity but finally succeeded.
Now tommorow I will finally make it possible to save the group and send all members a mail and write the group into the database.

#day 8
So where I thought I was pushing data from InviteActivity to MakeGroupActivity.
I forgot about the fact that when I add a member in InviteActivity and send it to MakeGroupActivity. I want to be able to add another person in InviteActivity. But when that happens MakeGroupActivity is closed and then I lose the members I already had send to that activity. I tried to work around it with sharedpreferences but this seemed unneccesarily difficult because I want to be able to send a arraylist and not just strings. This morning I realised I can just stay in MakeGroupActicity and on clicking the add member putton I can just use a AlertDialog instead of a complete new activity.
I will work on this Tommorow.

#day 8
Custom dialogs made, made start to sending out invitation emails for groupmembers you want to add and also writing to database.

#day 9
Struggled entire day with asynchronous firebase actions in a for loop...  Made some progress but still not where i should be.
Once this works other implementations should be easy. So basically what i was trying to do was the following. I wanted to be able to invite members by email. I wanted to be able to registrate these members straight away, write them into my database and send them an email. But because of asynchronous issues this did not work.

#day10
Worked around some of the asynchronous issues. By using a Future I can now authenticate members in a loop and send them mail. unfortunately it is still not possible to write them in the database with their user id. Because I only have acces to their uid when they are signed in or when they are in the authentication function of my code. But because of the asynchronous issues it never gets to that part in the code. I am thinking of solutions and spoke with Renske and Hella about it.

#day11
Still trying to figure out a solution. I am now thinking of just send members you want to invite an email. Then in my database under groups I have a list with groupmembers and here I put their email. So when they register and sign in I can find to what groups they have access by looping through alll groups in my database. Although this is not so elegant it will work.

#day12
After presentations I had a eureka moment. Instead of writing users into my databse with their user id I am know going to hash their email (since an email is unique as well). When the user is then signed in I can acces their email and hash it again and then search the database with that information.

#day13
Busy with weekend activities.

#day14
Busy with weekend activities.

#day15
Worked on reading and writing tasks into firebase as wel as seeing it in a listview. Made custom dialog to add tasks.
bumped into some bugs and solved some of those. one of the bugs i faced was that sometimes i was writing stuff double.
But only sometimes because usually it is overwritten on the rewrite. But in slow writing timestamps of id's changed so sometimes stuff was written double. I found mistakes in the code and solved this.
Tommorow I will work on deleting tasks groups and users as well as some other bugs.

#Day16
Made it possible to delete and edit tasks.
Delete rooms, and delete groups. But the deleting of groups still does not work properly. (again a asynchronous problem)
Also made custom dialogs to pop up on deleting to warn the user.
Made some adjustments to database structure to make editing and deleting easier.

#Day17
The MyAccountActivity now works and will show the user details, these can also be edited.
Also implemented a password reset.
Made a new ListView for design purposes but this still needs to be enhanced.
Worked on bug for my adapters. Data from firebase sometimes doesnt load, only loads on refresh of activity.
made some progress but still not completely working.
still needs to be done: deleting of groups (perhaps deleting of user as well)

Managed to solve the deleting of group. Now it deletes everything that has to be deleted properly!

So still left: Deleting of user, making it possible for a user to enter a group or leave a group after creation?
And fixing ListView problem..

#Day18
Did all that was left yesterdaty

#day19 
presentation. Added checkboxes and spinner. But no functions to it yet

#day 20&21 enjoyed the weekend

#day 22
Added functionality to checkboxes. Had to make objects for it and some minor databse changes.
Added Spinner and functionality also had to make some objects and minor databse changes.
Looks like the functionality is here now. So now refactoring and bug fixing.
Also changed two normal arrayadapters for firebaselistadapters. Saved a lot of code!
If only i found out about this way earlier.





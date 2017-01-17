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

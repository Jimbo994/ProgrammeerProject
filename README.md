## ProgrammeerProject

**StudentenSchoonmaakApp**

Everyone that is or has been a student or has lived in a house with several people must agree that cleaning is a disaster.
If you clean there is a big chance some one else will make a big mess in the house. Because you feel you are the only one cleaning,
you stop cleaning. And because of this the average house is a mess. 
The StudentenSchooonmaakApp is the solution to this!

In the StudentenSchoonmaakApp you can make a group. You can give this group a list of tasks to do with a deadline. For example cleaning the kitchen or mopping the floor. These tasks can then be checked by members of the group, and groupmembers can see who has done it and when. An additional functionality could be that tasks are assigned weekly to people from the group. In this way it is possible to see who does what in the household and you can punish a groupmember that does not fulfill his tasks. 

**Benefits for the User**

The user will have insight in what has to be done to clean their household as well as being able to track what their housemates are doing.
Because of this the group will be more motivated to do their cleaning.

**Similar apps** 

There are apps in the play store at the moment that focus on delegating and planning cleaning tasks in households.
Some well developed apps are [ChoreMonster](https://play.google.com/store/apps/details?id=com.choremonster.cmandroid) and [OurHome](https://play.google.com/store/apps/details?id=com.getfairshare.ourhome). Choremonsters focusses mainly on getting children to do chores in the house.
By doing this they can earn points to get rewards from their parents. This is not what I want to do with my app, although it might be nice to implement a scoreboard for students completing their tasks in time or punishments for those who don't.

Ourhome seems like a really decent application that basically covers all functionalities that I want to add in my application and more.
It is usable for a bigger audience than just students. However I feel that it is not really user friendly and it might have a bit too many functionalities which might make it a bit overcomplicated. 

Then there is [Clean House](https://play.google.com/store/apps/details?id=net.sloik.housechoresschedule) which is in my perhaps short-sighted opinion more of a to do list.
Lastly there is a app called [ChoreList](https://play.google.com/store/apps/details?id=com.jimbl.choreslistfrgoog) which is a more advanced version of Clean House with some additional functions. 
But it does not support the use of user groups and I feel that the design and UI are not really eye-catching.

Another app which targets students is [WieBetaaltWat](https://play.google.com/store/apps/details?id=nl.wiebetaaltwat.webapp). It is an app where you can make a group (by inviting people by e-mail) where you can load your receipts. With this information the app calculates the expends made by you and your groupmembers. The goals of this app is to make it clear who spends money on what so the users can equilibrate on money. What I like about this app is that the design is simple, yet visually attractive, and it works great. I hope to be able to make use of this in my application as well. Also the ability of making groups is really interesting for my app.


**Sketch and working of the app**

<img src="https://github.com/Jimbo994/ProgrammeerProject/blob/master/docs/sketch%20proposal.jpg" height="534" width="700"/>

**Necessities & Complications**

This app will need a database to store information of users, groups and tasks of these groups. Also multiple users of the same group should be able to acces this data. This will be a big challenge to overcome and perhaps the biggest. I am planning on using FireBase as a database and on first sight it seems that FireBase offers support for groups and e-mail activation. But this might be the biggest problem that has to be overcome in the construction of this application. 

WieBetaaltWat makes uses of account activition using email and also inviting other people by sending email. I would really like it if my app had this possibility as well. But I do not know how I can implement this yet. This is going to be a problem as well.


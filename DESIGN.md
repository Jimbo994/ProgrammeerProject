## Design

**Log In**

*LogInActivity*

In deze Activity kunnen gebruikers inloggen om naar het thuisscherm te gaan. Wanneer de knop "inloggen"is ingedrukt moet de ingevoerde data gecheckt worden met de firebase data om te kijken of het account bestaat of niet en of het wachtwoord correct ingevoerd is.

Als een gebruiker zijn of haar wachtwoord vergeten is kan er op "wachtwoord vergeten" worden gedrukt ForgotPasswordActivity word vervolgens geopend.

Als een gebruiker nog geen account heeft kan hij op registreren drukken en word hij doorverwezen naar RegisterActivity.

*ForgotPasswordActivity*

In deze activity kan een email ingevoerd worden. Wanneer de gebruieker op "herstel mijn wachtwoord" drukt, word er een email verstuurd met daarin zijn wachtwoord. Alleen als de opgegeven email daadwerkelijk in de database staat.

*RegisterActivity*

In deze Activity kan een gebruiker een account aanmaken. Hij vult hiervoor de vereiste edittexts in en klikt op maak account. Er word nu een mail gestuurd naar zijn opgegeven e-mail om zijn account te verifieren. Wanneer dit is gedaan word deze data geschreven op firebase.

<img src="https://github.com/Jimbo994/ProgrammeerProject/blob/master/docs/Design%20Login%201.jpg" height="650" width="1000"/>

**Home Screen**

*HomeScreenActivity*

This Activity is a navigation drawer activity. When the navigation drawer button (topleft) is clicked you can click on my account to see your account details. In this activity a user can create a group by clicking the plus botton, the user will be redirected to another activity. When a group is clicked, the user will be redirected to a activity showing the details of this group.

A listview with the group(s) a user is in should be loaded by checking the firebase database when the activity is created.

*AccountDetailActivity*

This Activity should show details of the users account, This information should be read from the firebase data base so again here on oncreate this data should be read from firebase. Users should also be able to sign out here as well as edit their password and possibly other information.

*MakeGroupActivity*

In this activity a user is able to make a group. The user can make a name for this group and add participants of this group by entering their mail. By doing this the invited user should get an email with which he can make an account and join the group.
Further research must point out if this is feasible and if firebase offers support for this.

<img src="https://github.com/Jimbo994/ProgrammeerProject/blob/master/docs/Design%20Homescreen%201.jpg" height="1000" width="1000"/>

**Huistaken**

*GroupActivity*

When a group is clicked on the homescreen this activity is opened. In here there is a listview with all the items that are a stored in the database in this group. On clicking the plussbotton a alertdialog will be opened where another item can be added to this list. This will be written into the database. 

(Perhaps a on longitem click listener can be set to support deleting)

When a item on the listview is clicked another activity will be opened showing the tasks that needs to be done in this item.

The idea is to make this activity and get it to work in this form to start with. But there are plans to make this activity more interesting by adding assignment of users and adding deadlines to tasks.

*TaskActivity*

In this Activity the tasks belonging to an item can be seen and checked by a user when done. It should be apparent what user clicked what task. Again this is the starting point. I want to expand functionalities but for know this is how im going to implement it.

<img src="https://github.com/Jimbo994/ProgrammeerProject/blob/master/docs/Design%20Huistaken.jpg" height="1000" width="1000"/>

**Additional Classes**

*FireBaseHelper*

There is going to be a FireBaseHelper which will do all reading/writing to the database.

**Rough structure of database**

  { <br>
  "groups": {<br>
    "Studenthouse1": {<br>
      "joe": true,<br>
      "sally": true<br>
    },<br>
    "Studenthouse2": {<br>
      "joe": true,<br>
      "fred": true<br>
    }<br>
  },<br>
  "data": {<br>
    "kitchen": {<br>
      "group": "Studenthouse1"<br>
      "tasks": "dishes", etc.<br>
      /* data accessible only by the "Studenthouse1" group */<br>
    },<br>
    "Bathroom": {<br>
      "group": "Studenthouse2"<br>
      /* data accessible only by the "beta" group */<br>
    },<br>
    "Living room": {<br>
      "group": "Studenthouse1"<br>
      /* more data accessible by the "alpha" group */<br>
    }<br>
  }<br>
  }<br>



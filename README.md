# DiscordBot

## Overview
Discord bot used in a personal server. Interfaces with Discord API (through `JDA`, the Java Discord API) to parse incoming messages (regex) and perform commands. Creates multithreaded server, which allows multiple clients to connect and execute commands remotely. Accepts commands from console. Uses Youtube Data API to stream audio into voice channels. Interact with users, user roles, and text/voice channels.

## Events
### Commands
All discord events trigger events in JDA, which are extended by the bot. When messages are sent, `onMessageRecieved` is triggered, and the command is parsed for a valid command. If one is found, it executes it with the provided parameters.

### State
Other listeners are used to determine the state of the bot, such as `onReadyEvent` or `onShutdownEvent`. These are used to initialize or properly kill various objects.

### CLI Commands
Extra administrative commands are provided, which can be executed through the command line (see gallery). These CLI commands allow the execution of commands from outside of Discord, and are the basis for the client/server capabilities of the bot.

## Client/Server
Basic socket programming allows for the communciation between the bot (server), and many other clients.
### Server
The bot is run in the terminal. When it starts, it acts as a server and listens for clients. When clients connect, they are able to execute commands remotely. The server allows for several clients to be connected at a time, each being listened to on a separate thread. When commands are recieved by the client, they are parsed for correctness, and if allowed, executed with the provided commands through the server's connection to the bot.
### Client
The client attempts to connect to the server, and if successful, awaits user input. Once input is given, the client writes to the server in attempt to execute a command.
### Protocol
No particular protocol is used to read/write from server/client. If I were to make any additional changes, I would create a formal protocol with static-size write/reads and expected responses between the server/client.

## Audio
### Overview
When the user executes a play command, the bot connects to the voice channel (provided the user was already in a voice channel) and streams audio from YouTube. Users may queue (`.play`) multiple videos, along with `.skip` and `.stop` tracks. The bot will automatically disconnect from voice after 5 minutes of inactivity.
### Direct 
A user may use the `.play <youtube-url>` to play a specific audio. This functionality is provided by LavaPlayer, which implements JDA's `AudioSendHandler`. LavaPlayer handles the transfer of audio packets to JDA, which outputs them through the virtual microphone of the bot. LavaPlayer requires solely the video id of the video to be retrieved. The audio id is retrieved from the user provided url in the command.
### Keywords
A user may use the `.play keyword1 keyword2...` command to play audio by entering only keywords. When a user enters this command, the YouTube Data API is used to search YouTube for the most relevant video. This is returned as json text, which is parsed for the title, duration, and video id. LavaPlayer is then used to stream the audio. The title and duration are used in an embedded message to provide feedback to the user.

## Gallery
### Client/Server
#### Server Console
![](https://imgur.com/mB39r5k.png)
#### Client Connecting
![](https://imgur.com/63DaKiZ.png)
#### Server Accepting Client
![](https://imgur.com/0JlvHfe.png)
#### Client Running Command
![](https://imgur.com/98HCCRg.png)
#### Server Received Command
![](https://imgur.com/IgAneGJ.png)

### Audio Usage
Notice the deletion and recreation of the embedded message.
#### Bot Connected
![](https://imgur.com/2N3bzvB.png)
#### Playing Track
![](https://imgur.com/JjeGOPc.png)
#### Queuing Additional Track
![](https://imgur.com/if8FM59.png)
#### Skip Track
![](https://imgur.com/l7IsS8w.png)
#### Stop Track
![](https://imgur.com/0Y34nl7.png)

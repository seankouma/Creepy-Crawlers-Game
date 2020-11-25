# Creepy-Crawlers-Game

![](name-of-giphy.gif)
(Please note, you might have to wait a second before the above gif plays at normal speed.)

This 2-D tile game was an idea I had based off of a game that I used to play when I was younger that was poorly made and had a huge lack of gameplay. The premise is that you control a crawler in the game that is a certain number of tiles in length and your goal is to become as long as possible. There are other crawlers moving around the board (both larger and smaller) that are played by the computer and you need to eat them while avoiding being eaten yourself! If you expeience a head on collision with another crawler then whichever one of you is longer consumes the other one entirely. Otherwise, if you're able to hit another crawler in their mid-section you can slowly eat them away until they're smaller than you.

In the gif at the top, I'm currently controlling the crawler that begins above the other crawler, while the computer is making moves for the in the bottom left.

I started this probject about a year and a half ago, but no major modifications have been made in the past year. I was able to get most of the basic functionality working (creating an editable background, controlling your own crawler while the others are controlled by the computer (which just makes random decisions at each junction), etc.) but there are still some large bugs that need to be worked out.

If I'm also being totally honest. Going through the code of this program makes me cringe a little bit (okay, a lot) now. There aren't good comments, there's unnecessary code duplication, and there are some serious design flaws in the way things are organized in it.

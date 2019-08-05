William Sands

Project Overview-
	In this project, we are asked to build a map off of a text file input, given all roads, and intersections, along with the Long. and Latt. data for each intersections, and what intersections each
	road connects. To solve this project, I placed all the data into a graph, using intersections as vertex and roads as edges. From this graph, I constructed an onscreen graphical map using a scaling
	function in such a way that each latt and long value mapped to a value within the window, so that the windows could be dynmamically resized. From there, I wrote my own implamentation of Dijkstra's
	shortes path algorythm that would find the shortest path to the vertex. To inprove the speed, especially in cases where the two vertex are not across the map, I set the Dijkstra's to terminate
	once the desired end vertex had been marked as done(meaning that the current cost of traveling to that node was the lowest possible cost). I then have the map update with a red line to show
	the shortest possible path. Added nice message that log to console the ammount of time it takes to build the graph, and find the shortest path. 
	In addition, a GPS style readout is generated to Directions_StartIntersection_EndIntersection.txt giving the roads to follow that connect the two desired intersections, in traditonal map direction style. 
Problems Encountered-	
	Initally I found a graph class online and was using that for my implamentation, but I notieced a pretty big failing of this program. While it works well for demonstraion pupposes,
	it stored all the vertex in a linked list. This works well for small graphs, when looking up the two vertex to add an edge to them doesn't take too much time, but this solution
	has no scaling ability. Initially, building the graph of New York State took roughly 20 minutes. By storing all vertex in a HashMap instead, I was able to dramaitcally cut back on the runtime
	(see runtime analyis of plotting map) and was able to build the graph of New York State in just under 1 second. 
	Annother problem I encountered was how to properly scale the coordinates to the applet screen. I initally just tried drawing them to their long. and latt. locations, but this created a extrememly
	tiny map that didn't scale to the screen. Instead of this, I found a scaling function I could use, so that when i draw the map, I find the Max and Min values for both X and Y, and scale all
	other points accordingly. To make this super efficant, I created a sepreate mapping object for each vertex that additionnally stored X and Y location. Then, by storing these in a hashmap, i could
	quickly find the location of the two endpoints for each road, and then just cycle through the list of all roads. I believe this shows the real-world usages for such a program, and as such deserves extra credit.

Expected runtime of plotting map
	Constructing the graph for the map adds all vertex to a hashtable (O(n) where n is number of vertex) and then for each of edges  looks up the vertex in the table using the
	two vertex associated id's (takes O(n) time average where n is the number of edges since hashtable lookups average constant time). Building the physical graph works the same way. 
	Drawing all the edges on screen first takes all vertex and scales them to the current window size (O(n) where n=number of vertex) and then draws all edges using a hashtable again to look up the edge's 
	two endpoint coordinates on the xy grid (takes average O(n) time wehre n is number of edges).   

Expected runtime of shortest path
	Since I used a min-heap for the shortes path, the runtime of finding the shortest path is (O((E+V)*logV) where E is Edges and V is vertex) since adding all vertex takes V time, pulling a vertex 
	and replacing in the que takes logV time since I am using a min-heap as my priority que. At most, these removals and inserts happen for all of the edges pointing to a vertex, maing this a 
	ElogV operation. Since the program goes through and removes all vertex and then checks to see if the que is empty, this will take VlogV time. Adding these two operations together, we get 
	ElogV for updating distance, and VlogV for pulling vertex and chekcing if the que is empty -> ElogV+VlogV or (E+V)logV. That is how I solved for the runtime. 

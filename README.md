# Expeditors
Expeditors Developer Design and Development exercise 1.2 solution for Bryce Meadors

# Process
After reading the description of the project, I noticed that I would need to filter out age groups and sort by last name, then first name. Immediately my first through was "I can do that in SQL", so I decided to make a SQLite database. In order to make the data easier to compared, I used a Scanner.useDelimiter that properly parsed the input data to get the appropriate Strings.
Once the data was cleaned and prepared, I was able to parse through the assigned ArrayList<String> and place each value in the appropriate section of the database.
Once the database was filled, it was simple enough to query it and print to the console.

# Assumptions
Because the description was pretty open-ended about the input data, I decided that it would be safe to assume that the input file would be clean and formatted the same as the given input data. That is: 6 Strings per line where each line has FIRST NAME, LAST NAME, STREET ADDRESS, CITY, STATE, AGE. To make the data entry easier, I conversted the 6th element in each line to an int. This allowed me to read a file in, rather than hard code data entry values, and also makes is more easily scalable, with assumptions.

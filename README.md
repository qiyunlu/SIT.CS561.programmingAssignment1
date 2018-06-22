# SIT CS561 Assignment 1

### Generate 2 separate reports based on the following queries (one report for query #1 and another for query #2):
1. For each customer, compute the maximum and minimum sales quantities along with the corresponding products, dates (i.e., products for those maximum and minimum purchases, and the dates when those maximum and minimum sales quantities were made) and the states in which the sale transactions took place. If there are >1 occurrences of the max or min, choose one – do not display all. For each customer, also compute the average sales quantity.
2. For each combination of customer and product, output the maximum sales quantities for January (regardless of the year, that is, both 1/11/2000 and 1/23/2008 are considered sales transactions for January) and minimum sales quantities for February and March (again, regardless of the year) in 3 separate columns. Like the first report, display the corresponding dates (i.e., dates of those maximum and minimum sales quantities). Furthermore, for January (MAX), include only the sales that occurred between 2000 and 2005; for February (MIN) and March (MIN), include all sales.

### The following is a sample output – quantities displayed are for illustration only (not the actual values).
![](https://github.com/qiyunlu/SIT.CS561.programmingAssignment1/raw/master/Example.png)

### Make sure that:
1. “select * from sales” is the ONLY SQL statement allowed in your program.
2. Character string data (e.g., customer name and product name) are left justified.
3. Numeric data (e.g., Maximum/minimum Sales Quantities) are right justified.
4. The Date fields are in the format of MM/DD/YYYY (i.e., 01/02/2002 instead of 1/2/2002).
5. You are NOT allowed to read in the entire table (‘sales’) and store them in memory before processing the rows. Instead, you need to read each row (one row at a time), process it and discard it.

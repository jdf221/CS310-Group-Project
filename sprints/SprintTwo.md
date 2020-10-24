# Sprint Two

We will be completing Features 3 and 4 along with cleaning up some code written in Sprint 1.

The assignments:

* Alex: Clean up the Shift.java code by changing variable names to be more descriptive and use LocalTime and Duration for the time calculations.
* Hunter: Write the `calculateTotalMinutes()` function described in Feature 4 on Canvas.
* Ryan: Write the `adjust()` function described in Feature 3 on Canvas.

___

## Shift.java

The time calculations were being done incorrectly previously, so you need to fix that.

I recommend using the `LocalTime.parse()` and `Duration.between()` functions. Using LocalTime will also be a huge help during Feature 3.

## Feature 3

This feature is pretty complicated to understand so just message me if you get stuck. It's tedious to implement.

It's mainly just a bunch of ifs checking if a time is between 2 other times.

### Feature 4

The wording on this one is very confusing. But all that needs to be done is to add up the minutes between clock in and clock out punches, then if there were only 2 punches (meaning they did not take a lunch break) and they worked for more than the shift duration you must subtract the lunch duration from the minutes worked.
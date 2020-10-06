# Sprint One

We will be completing feature 1 and 2 in this first sprint.

I have assigned one file/class to each of you guys to work on.

* Alex: Badge.java
* Hunter: Shift.java
* Ryan: Punch.java

Each file needs to be updated to match what the unit tests expect. I have scaffolded out the required functions within each class so all you have to do is write the code to make the functions return or do what is expected.

I have created unit tests for each class, you can run the specific unit test for your file to test your code.

If you have any questions or are struggling please text (or email) me.

___

Below is what needs to be done in each file:

## Badge.java

The Badge class is expected to override the toString function and have it return a string in the following format

```
#badgeId (employeeName)
```

The class also includes a `getBadgeId()` function. This function should return the badgeId that is passed in through a argument in the constructor.

To complete this class you will need to create 2 private class variables.

## Shift.java

The Shift class needs to override the `toString()` function and return a string in the following format

```
Shift 2: 12:00 - 20:30 (510 minutes); Lunch: 16:30 - 17:00 (30 minutes)
```

## Punch.java

The Punch class needs to be finished so that every function is completed and does what is expected. The timestamp should default to the current timestamp and be settable using the `setOriginaltimestamp()` function.

`printOriginalTimestamp()` should return a string in the following format:

```
#badgeId CLOCKED IN: WED 09/05/2018 07:00:07
```
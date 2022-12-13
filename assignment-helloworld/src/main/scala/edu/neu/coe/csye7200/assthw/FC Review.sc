import java.util.Date

val f1 = Math.atan2 _
f1(1, 0)

def swapper[T1, T2, R](f: (T1, T2) => R): (T2, T1) => R =
  (t2, t1) => f(t1, t2)

val f2 = swapper(f1)
f2(0, 1)

val ts: Seq[(Double, Double)] = Seq((4,3), (0,1), (0.5, 1))
// library function that yields points (x,y)
def point(x: Int): (Double, Double) = ts(x)

// If we want to get it's angle we can't because atan2 expects (y, x)
val f3: ((Double, Double)) => Double = f2.tupled
//ToTuple has a swap method that could be used on point(x) if we wanted
val g: Int => Double = x => f3(point(x))
g(0)


//currying
case class Date(year: Int, month: Int, day: Int)

val fDate: (Int, Int, Int) => Date = Date.apply
val fDateCurried: Int => Int => Int => Date = fDate.curried

// We care about birthdays, we don't care about the year

def compareBirthdays(x1: Date, x2: Date): Boolean =
  x1.month == x2.month && x1.day == x2.day

compareBirthdays(fDateCurried(2000)(1)(1), fDateCurried(2001)(1)(1))

// One int is gone now because we've made it partially applied
val g1: Int => Int => Date = fDateCurried(2000)
val g2: (Int, Int) => Date = Date.apply(2000, _, _)


def reverse[T1, T2, T3, R](f: T1 => T2 => T3 => R): T3 => T2 => T1 => R =
  t3 => t2 => t1 => f(t1)(t2)(t3)

val g3 = reverse(fDateCurried)
// Function that takes a year, reversing because order matters so we couldn't have done with g1/g2
val h: Int => Date = g3(31)(1)

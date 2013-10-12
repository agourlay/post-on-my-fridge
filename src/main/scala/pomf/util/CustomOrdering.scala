package pomf.util

import org.joda.time.DateTime

object CustomOrdering {
   implicit def dateTimeOrdering: Ordering[DateTime] = Ordering.fromLessThan(_ isBefore _)
}
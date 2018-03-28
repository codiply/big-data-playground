package com.codiply.kafkaplayground.util

// This code is copied from here: https://stackoverflow.com/a/16363444

object DelayedFuture {
  import java.util.{Timer, TimerTask}
  import java.util.Date
  import scala.concurrent._
  import scala.concurrent.duration.FiniteDuration
  import scala.util.Try

  private val timer = new Timer(true)

  private def makeTask[T]( body: => T )( schedule: TimerTask => Unit )(implicit ctx: ExecutionContext): Future[T] = {
    val prom = Promise[T]()
    schedule(
      new TimerTask{
        def run() {
          // IMPORTANT: The timer task just starts the execution on the passed
          // ExecutionContext and is thus almost instantaneous (making it
          // practical to use a single  Timer - hence a single background thread).
          ctx.execute(
            new Runnable {
              def run() {
                prom.complete(Try(body))
              }
            }
          )
        }
      }
    )
    prom.future
  }
  def apply[T]( delay: Long )( body: => T )(implicit ctx: ExecutionContext): Future[T] = {
    makeTask( body )( timer.schedule( _, delay ) )
  }
  def apply[T]( date: Date )( body: => T )(implicit ctx: ExecutionContext): Future[T] = {
    makeTask( body )( timer.schedule( _, date ) )
  }
  def apply[T]( delay: FiniteDuration )( body: => T )(implicit ctx: ExecutionContext): Future[T] = {
    makeTask( body )( timer.schedule( _, delay.toMillis ) )
  }
}

package models

import play.api.db.DB
import anorm.Pk
import anorm.NotAssigned

case class Motorcycle( id :Pk[Long]=NotAssigned, make: String, model: String, engineCapacity: Int)


/**
 * A sort DAO for for the case class Motorcycle.
 */
object Motorcycle {

  import anorm.SQL
  import anorm.SqlQuery

  val sql: SqlQuery = SQL("select * from motorcycles order by make,model asc")

  import play.api.Play.current

  def getAll: List[Motorcycle] = DB.withConnection {

    implicit connection =>

      sql().map(row =>

        Motorcycle(row[Pk[Long]]("id"), row[String]("make"), row[String]("model"),
          row[Int]("engineCapacity"))).toList
  }

  def insert(motorcycle: Motorcycle): Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""insert
            into motorcycles
            values (
            (select next value for motorcycles_seq), 
            {make}, {model}, {engineCapacity})""").on(
            "make" -> motorcycle.make,
            "model" -> motorcycle.model,
            "engineCapacity" -> motorcycle.engineCapacity).executeUpdate()==1
    }
  }

  def update(id:Long, motorcycle: Motorcycle):Boolean = {
    DB.withConnection { implicit connection =>
      SQL("""update motorcycles
            set make = {make}, model = {model}, engineCapacity = {engineCapacity}
            where id = {id}""").on(
          "make"-> motorcycle.make,
          "model" -> motorcycle.model,
          "engineCapacity"-> motorcycle.engineCapacity,
          "id" -> id)
          .executeUpdate() ==1
    }
  }
  
  
  def findByMakeAndModel(motorcycle:Motorcycle):Option[Motorcycle] = 
    DB.withConnection { implicit connection =>
      val sql = SQL("select * from motorcycles where make={make} and model={model}").
      on("make" -> motorcycle.make,
         "model" -> motorcycle.model
      )
      
     sql().map(row =>
        Motorcycle(row[Pk[Long]]("id"), row[String]("make"), row[String]("model"),
          row[Int]("engineCapacity"))).headOption
            
  }
  
 def findById(id: Long): Option[Motorcycle] = {
    DB.withConnection { implicit connection =>
      val sql =SQL("select * from motorcycles where id = {id}").on('id -> id);
       sql().map(row =>

        Motorcycle(row[Pk[Long]]("id"), row[String]("make"), row[String]("model"),
          row[Int]("engineCapacity"))).headOption
      
    }
  }
 
 def delete(id: Long) = {
    DB.withConnection { implicit connection =>
      SQL("delete from motorcycles where id = {id}").on('id -> id).executeUpdate()
    }
  }
 
}







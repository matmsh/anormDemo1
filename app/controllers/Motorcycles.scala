package controllers

import models.Motorcycle
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Action
import play.api.mvc.Controller


object Motorcycles extends Controller{

    // When adding a new motorcycle, it must not already exists in the db.     
   lazy val motorcycleMapping=mapping(
        "make" -> nonEmptyText,
        "model" -> nonEmptyText,
        "engineCapacity" ->  number
        )(Motorcycle.apply)(Motorcycle.unapply).verifying(
            "Make and model already exists.", motorcycle => !exists(motorcycle))
        
   val motorcycleForm = Form[Motorcycle](motorcycleMapping)  
  
   
   def list = Action {
       val bikes = Motorcycle.getAll;
       Ok(views.html.list(Motorcycle.getAll))
   }
   
   def newMotorcycle = Action{
      Ok(views.html.add(motorcycleForm))
   } 
   
   def create = Action {
     implicit request =>
        val motorcycleFormNew = this.motorcycleForm.bindFromRequest;
        motorcycleFormNew.fold(
          hasErrors = {formWithError =>
                     Ok(views.html.add(formWithError)) },
                     
          success = {  motorcycle => {
                                       Motorcycle.insert(motorcycle);
                                       Redirect(routes.Motorcycles.list) }
          }
       )
        
     
   }
   
   
   private def exists(motorcycle:Motorcycle):Boolean ={
      val ans = Motorcycle.findByMakeAndModel(motorcycle)
      println("ans=" + ans)
      ans != None
   }
}
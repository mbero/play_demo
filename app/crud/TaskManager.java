package crud;

import java.util.List;

import javax.inject.Singleton;

import models.Task;

import com.avaje.ebean.Ebean;
import play.Logger;
/**
 * Defines basic CRUD operations using Ebean ORM in the simplest possible way..
 * @author Marcin Berendt
 *
 */
@Singleton
public class TaskManager {
	
	 public void insertTask(Task task)
	 {
		Ebean.insert(task);
	 }
	 
	 public Task getTaskById(Long taskId)
	 {
		 Logger.debug("TaskManager.getTaskById() - started");
		 Task properTask = null;
		 try
		 {
			 properTask =  Task.find.ref(taskId);
		 }
		 catch (Exception e)
		 {
			 Logger.debug(e.getMessage());
		 }
		 
		 Logger.debug("TaskManager.getTaskById() - finished");
		 return properTask;
	 }
	 
	 public List<Task> getAllTasks()
	 {
		 Logger.debug("TaskManager.getAllTasks() - started");
		 List<Task> allTasks = Task.find.all();
		 Logger.debug(allTasks.size() + " Tasks finded");
		 Logger.debug("TaskManager.getAllTasks() - finished");
		 return allTasks;
	 }
	 
	 public void deleteTask(Long taskId)
	 {
		 Logger.debug("TaskManager.getAllTasks() - started");
		 Logger.debug("Task with id : " + taskId + " will be deleted from DB");
		 Task.find.ref(taskId).delete();
		 Logger.debug("TaskManager.getAllTasks() - finished");
	 }
}

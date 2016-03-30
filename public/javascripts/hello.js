if (window.console) {
  console.log("Welcome to your Play application's JavaScript!");
}  

$(document).ready(function(){
	 $.ajax({
	      url: '/recentous',
	      type: 'GET',
	      data: 
	      {
	      },
	      success: function (response) 
	      {
	    		for(var i=0; i<response.length; i++)
	    	    {
	    			var currentElementFromResponse = response[i]
	    			$('#hatch ul').append('<li><a href="#" data-maker="'+currentElementFromResponse+'" data-price="'+currentElementFromResponse+'">'+currentElementFromResponse+'</a></li>'); 
	    	    }
	      }
	  });
	  $('a').on('click', function(){
  	    $('#show').html(   'Price : ' + $(this).attr('data-price') + '| Maker : ' +  $(this).attr('data-maker')   );
  	  });

});

function findROsByKeyword()
{
	var keywordFromInput = $('#keyword').val();
	 $.ajax({
	      url: '/getousbykeyword',
	      type: 'GET',
	      data: 
	      {
	    	 keyword : keywordFromInput
	      },
	      success: function (response)
	      {
	    	  $('#findByKeywordResultTable').bootstrapTable("destroy");
	    	  $('#findByKeywordResultTable').bootstrapTable({
	    		    columns: [{
	    		        field: 'name',
	    		        title: 'RO Name'
	    		    }, {
	    		        field: 'uri',
	    		        title: 'RO URI'
	    		    }
	    		    ],
	    		    data: response
	    		});
	      }
	  });
}

function showAllTasksInTable()
{
	  var jsonData;
	  $("#records_table tr").remove();
	  $.ajax({
	      url: '/getalltasksjson',
	      type: 'GET',
	      data: 
	      {
	      },
	      success: function (response) {
	    	  var trHTML= 
	    	    '<tr><td>' + 
	    	    'Id '+ '</td><td>' + 
				'Name' + '</td><td>' + 
				'Due Date' + 
				'</td></tr>';
	          $.each(response, function (i, item) 
	          {
	              //trHTML += '<tr><td>' + item.name + '</td></tr>';
	        	    trHTML += '<tr><td>' + item.id + '</td><td>' + 
	        	    					item.name + '</td><td>' + 
	        	    					item.dueDate + '</td></tr>';
	          });
	          $('#records_table').append(trHTML);
	      }
	  });
}

function addNewRandomTaskToDB()
{
	 $.ajax({
	      url: '/inserttaskandreturnjson',
	      type: 'POST',
	      data: 
	      {
	      },
	      success: function (response) {
	    	  showAllTasksInTable();
	      }
	  });
}

function deleteTaskById()
{
	 var id = $('#taskid').val();
	 $.ajax({
	      url: '/deletetask',
	      type: 'GET',
	      data: 
	      {
	    	  id:id
	      },
	      success: function (response) {
	    	  showAllTasksInTable();
	      }
	  });
}



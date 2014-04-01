
//To avoid send enter
	function disableEnter(e) {
	  tecla = (document.all) ? e.keyCode : e.which;
	  return (tecla != 13);
	}	

	function MM_findObj(n, d) { //v4.01
	  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
	    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
	  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
	  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=MM_findObj(n,d.layers[i].document);
	  if(!x && d.getElementById) x=d.getElementById(n); return x;
	}

	function showhide(id) {
		var obj = MM_findObj(id);
		if(obj.style.display=="none")
			obj.style.display="";
		else
			obj.style.display="none";
	}
	
	function show(id) {
		MM_findObj(id).style.display = "";
	}

	function hide(id) {
		MM_findObj(id).style.display = "none";
	}
	
	function dialogMessage(message, className){
		//Create div dialogMessage if not Exist
		if(!MM_findObj('dialogMessage')){
			dialogDiv = document.createElement("div");
			dialogDiv.id='dialogMessage';
			dialogDiv.title='Fi-Ware';
			dialogDiv.className='dialog';
			document.body.appendChild(dialogDiv);

			$('#dialogMessage').dialog({
				autoOpen: false, modal: true, position:'top', width:400,
				buttons: { "Ok": function() { 
						$(this).dialog("close"); 
					}  
				}
			});
		}
		
		className = (className==null) ? 'error':className;
		dialogDiv.title=className;
		$('#dialogMessage').dialog('open');
		$('#dialogMessage').html('<p class="'+className+'">'+message+'</p>');
		
	}
	
	function del(url){

		var arrayPos = new Array();
		
		//Create div confirmDelete if not Exist
		if(MM_findObj('confirmDelete')==null){
			var delDiv = document.createElement("div");
			delDiv.id='confirmDelete';
			delDiv.title='Fi-Ware';
			delDiv.className='dialog';
			document.body.appendChild(delDiv);
			
			$('#confirmDelete').dialog({
				autoOpen: false, modal: true, position:'top',
				buttons: { "Cancel": function() { 
						$(this).dialog("close"); 
					}, "Ok" : function() { 
						cargarIconoLoading();
						document.forms.strutsExtForm.action = url+"&action=delete&arrayPos="+arrayPos;
						document.forms.strutsExtForm.submit();
					}  
				}
			});
		}
		
		var cont;
		var actual = false;
		var checkSelected = 0;
		var id;
		var tamChecks = document.getElementsByName("checks").length;
		var posi = 0;
		var posiArray;
		for(cont=1;cont<=tamChecks;cont++){
			actual = MM_findObj("checks"+cont).checked;
			if (actual) {
				checkSelected++;
				posiArray = cont - 1;
				arrayPos[posi] = MM_findObj("checks"+cont).value;
				posi++;
			}
		}
		if (checkSelected > 0) {
			$('#confirmDelete').dialog('open');
			$('#confirmDelete').html('<p class="warning">Are you sure to delete?</p>');
		} else {
			dialogMessage("You must select a Register.", "info");
		}
	}
	
	//Function update register by default
	function upd(url, name, width, height){
		width = (width==null) ? 300 : width;
		height = (height==null) ? 300 : height;
		var cont;
		var actual = false;
		var checkSelected = 0;
		var id;
		var tamChecks = document.getElementsByName("checks").length;

		//alert(tamChecks);
		for(cont=1;cont<=tamChecks;cont++){
			actual = MM_findObj("checks"+cont).checked;
			//alert(actual);
			if (actual) {
				checkSelected++;
				id = MM_findObj("checks"+cont).value;
			}
		}
		if (checkSelected == 1) {
    		win=parent.dhtmlwindow.open("winbox", "iframe", url+'&action=SaveOrUpdateView&id='+id, name, "width="+width+"px,height="+height+"px,resize=1,scrolling=1,center=1", "recal");
		} else {
			dialogMessage("You must select one Register.", "info");
		}
	}
	
	//Action Insert
	function insert(url, name, width, height){
		width = (width==null) ? 300 : width;
		height = (height==null) ? 300 : height;
		win=parent.dhtmlwindow.open("winbox", "iframe", url+'&action=SaveOrUpdateView', name, "width="+width+"px,height="+height+"px,resize=1,scrolling=1,center=1", "recal");
	}

	
	function detalle(url, name, catDesc, width, height){
		width = (width==null) ? 300 : width;
		height = (height==null) ? 300 : height;
		var url = url+"&action=description&desc="+catDesc;
		win=parent.dhtmlwindow.open("winbox", "iframe", url, name, "width="+width+"px,height="+height+"px,resize=1,scrolling=1,center=1", "recal");
	}

	function openWin(url, name, width, height, winId){
		width = (width==null) ? 300 : width;
		height = (height==null) ? 300 : height;
		winId = (winId==null) ? "winbox" : winId;
		return parent.dhtmlwindow.open(winId, "iframe", url, name, "width="+width+"px,height="+height+"px,resize=1,scrolling=1,center=1", "recal");
	}		
	  
	 function isNumber(obj) {
		  	obj.style.backgroundColor="#FFFFFF";
		  	var number=true;
		  	//Comprobamos que no tenga mas de una parte decimal.
		  	if(obj.value.split(".").length>2)
		  		number=false;
		  		
			for (var i = 0; i<obj.value.length && number; i++) {
				var oneChar = obj.value.substring(i, i + 1);
				if (oneChar!="." && (oneChar < "0" || oneChar > "9")) {
					number=false;
			   }
			}
			if(!number)
				obj.style.backgroundColor="#FF8383";//"#FF0000";
			
			return number;
		}
	  
	  function checkDates(listInputs){
		    var inputsNames = listInputs.split("|");
		    var valid = true;
		    for (i=0; i < inputsNames.length; i++){
		    	var obj = MM_findObj(inputsNames[i]);
		    	valid = valid && validDate(obj);
		    }
		    return valid;
		  }

	  function validDate(obj){
		  var valid = true;
		  if(obj!=null){
			  	//obj.style.borderColor="#D9D9D9";
	    		obj.style.backgroundColor="#FFFFFF";
	    		if(!esFechaValida(obj.value)){
	    			//obj.style.borderColor="#FF8383"; 
	    			obj.style.backgroundColor="#FF8383";
	    			valid=false;
  			}
		  }
		  return valid;
	  }
	  
	  function validMonthDate(obj){
		  var valid = true;
		  if(obj!=null){
			  	//obj.style.borderColor="#D9D9D9";
	    		obj.style.backgroundColor="#FFFFFF";
	    		if(!isValidMonthDate(obj.value)){
	    			//obj.style.borderColor="#FF8383"; 
	    			obj.style.backgroundColor="#FF8383";
	    			valid=false;
  			}
		  }
		  return valid;
	  }
	  
	  function isValidMonthDate(fecha){
          if (fecha != undefined && fecha!= "" ){
              if (!/^\d{2}\/\d{4}$/.test(fecha)){
                  return false;
              }
              var mes  =  parseInt(fecha.substring(0,2),10);
              var anio =  parseInt(fecha.substring(3),10);
              return true;
          }
	}

	  function comprobarSiBisisesto(anio){
		  var bisiesto=false;
		  if ( ( anio % 100 != 0) && ((anio % 4 == 0) || (anio % 400 == 0))) bisiesto=true;
		  	return bisiesto;
	  }


		function esFechaValida(fecha){
	          if (fecha != undefined && fecha!= "" ){
	              if (!/^\d{2}\/\d{2}\/\d{4}$/.test(fecha)){
	                  return false;
	              }
	              var dia  =  parseInt(fecha.substring(0,2),10);
	              var mes  =  parseInt(fecha.substring(3,5),10);
	              var anio =  parseInt(fecha.substring(6),10);
		          switch(mes){
		              case 1: case 3: case 5: case 7: case 8: case 10:
		              case 12:
		                  numDias=31;
		                  break;
		              case 4: case 6: case 9: case 11:
		                  numDias=30;
		                  break;
		              case 2:
		                  if (comprobarSiBisisesto(anio)){ numDias=29 }else{ numDias=28};
		                  break;
		              default:
		                  return false;
		          }
	              if (dia>numDias || dia==0){
	                  return false;
	              }
	              return true;
	          }
		}
		
		function dateGreaorEqualThan(fec0, fec1){			   
			   var splitF1 = fec0.split('/');
			   var splitF2 = fec1.split('/');
			   var fecha0 = new Date(splitF1[1],splitF1[0],'01');
			   var fecha1 = new Date(splitF2[1],splitF2[0],'01');
			   return fecha0.getTime() >= fecha1.getTime();
		   }

	   function fechaMayorOIgualQue(fec0, fec1){
		   var splitF1 = fec0.value.split('/');
		   var splitF2 = fec1.value.split('/');
		   var fecha1 = new Date(splitF1[2],splitF1[1],splitF1[0]);
		   var fecha2 = new Date(splitF2[2],splitF2[1],splitF2[0]);
		   return fecha1.getTime()>=fecha2.getTime();
	   }
	  
	  
	  function pulse(e, nameFunction)
	   {
	      var evt = e ? e : event;
	      var key = window.Event ? evt.which : evt.keyCode;
	      if(key==13) // tecla enter
	       {
	       nameFunction();
	       }
	 }	  
	

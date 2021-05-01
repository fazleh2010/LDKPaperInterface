<!DOCTYPE html>
<html>

    <head>
        <style>
            .button {
                background-color: #4CAF50;
                border: none;
                color: white;
                padding: 15px 32px;
                text-align: center;
                text-decoration: none;
                display: inline-block;
                font-size: 16px;
                margin: 4px 2px;
                cursor: pointer;
            }
	</style>

     <style>
      table, th, td {
      border: 1px solid black;
     }
    </style>
    </head>

    <body>
<h2>Search a lexical item</h2>
 <!--Make sure the form has the autocomplete function switched off:-->
        <form action="index.php" method="post">
            <div class="autocomplete" style="width:300px;">
                <textarea id="myInput"  cols="100" rows="2" type="text" name="searchBox" placeholder="" style="font-size: 14pt"></textarea>
            </div>
            <!--input id="submit" type="submit"-->
            <!--input type="button" value="FindAnswer"-->
            <!--input type="text" name="searchBox" id="name" ><br/-->
           <input type="submit" name="submit" value="Submit">
        </form>
        <!--textarea id="answerTextBox"  cols="150" rows="20" style="font-size: 14pt"-->
        <!--?php print_r ($textt);?-->
        <!--/textarea-->
        <!--p>The machine is stopped.</p-->


   <table>
  <tr>
    <th>Property object pair</th>
    <th>Class</th>
    <th>Cosine</th>
    <th>CondAB</th>
    <th>CondBA</th>
    <th>SupA</th>
    <th>SupB</th>
    <th>PosTag</th>
  </tr>

        
<?php
       if($_SERVER['REQUEST_METHOD'] == "POST" and isset($_POST['someAction']))
    {
        func();
    }
       
        
     if(isset($_POST['submit']))
     {
        $givenToken = $_POST['searchBox'];
        print ($givenToken);
        $text =func($givenToken);
     } 



    function func($givenToken)
    { 
	$javaCommand ="java -jar";
        $dir ="/home/elahi/new/LDKPaperInterface/";
        $jarFile ="target/LDKPaperInterface-1.8.jar";
        $rule ="predict_po_for_s_given_localized_l";
        $className ="Politician";
        //$givenToken ="australian";
        $tokenStr ="\"".$givenToken."\"";
        $space =" ";
        $command =$javaCommand.$space.$dir.$jarFile.$space.$rule.$space.$tokenStr;
        $textt = shell_exec($command);
        //echo $textt;
        //echo str_replace("$","\n",$textt);

        $myArray = explode(PHP_EOL, $textt);
        $questions = array();
        $answers = array();
        $x = 0;


	

        foreach ($myArray as $value) {
		//list($ques, $ans) = array_pad(explode("+", $value), 2, null);
             list($kb, $class,$interestingness,$condAB,$condBA,$supA,$supB,$PosTag) = array_pad(explode("+", $value), 8, null);
            $questions[$x] = $ques;
	    $answers[$x] = $ans;
            echo "<tr>";
            echo "<td> $kb</td>";
	    echo "<td> $class</td>";
	    echo "<td> $interestingness</td>";
	    echo "<td> $condAB</td>";
	    echo "<td> $condBA</td>";
	    echo "<td> $supA</td>";
	    echo "<td> $supB</td>";
	    echo "<td> $PosTag</td>";
	    echo "</tr>";
            //echo $ques.'  '.$ans.'  '.$x.'<br/>'; 
            $x++;
	}
	    echo "</table>";

        $questions = '["' . implode('", "', $questions) . '"]';
        $answers = '["' . implode('", "', $answers) . '"]';
        return $textt;


        // do stuff     
    }


       // Call PHP function from javascript with parameters
        function ex(){
        
        }

        ?>

</table>

        <script type="text/javascript">


            const button = document.querySelector('input');
            const paragraph = document.querySelector('answer');

	    //var questions = <?php echo $questions; ?>;

	     var questions = <?php echo $questions; ?>;

            var answers = <?php echo $answers; ?>;
            window.termUrls = new Map();

            for (i = 0; i < questions.length; i++) {
                window.termUrls.set(questions[i], answers[i]);
            }

            let arr = Array.from(termUrls.keys());

            window.valueOfTextField = "";
	    window.text = "";
	    window.testText = "Test";
            document.getElementById("myInput").style.borderColor = "blue";

            autocomplete(document.getElementById("myInput"), arr);


            function autocomplete(inp, arr) {
                /*the autocomplete function takes two arguments,
                 the text field element and an array of possible autocompleted values:*/
                var currentFocus;
                /*execute a function when someone writes in the text field:*/
                inp.addEventListener("input", function (e) {
			var a, b, i, val = this.value;
	            	var x="<?php ex(); ?>";
			alert(x);
			window.testText =x;
                    /*close any already open lists of autocompleted values*/
                    closeAllLists();
                    if (!val) {
                        return false;
                    }
                    currentFocus = -1;
                    /*create a DIV element that will contain the items (values):*/
                    a = document.createElement("DIV");
                    a.setAttribute("id", this.id + "autocomplete-list");
                    a.setAttribute("class", "autocomplete-items");
                    /*append the DIV element as a child of the autocomplete container:*/
                    this.parentNode.appendChild(a);
                    /*for each item in the array...*/
                    for (i = 0; i < arr.length; i++) {
                        window.text = arr[i];
                        /*check if the item starts with the same letters as the text field value:*/
                        if (arr[i].substr(0, val.length).toUpperCase() == val.toUpperCase()) {
                            /*create a DIV element for each matching element:*/
                            b = document.createElement("DIV");
                            /*make the matching letters bold:*/
                            b.innerHTML = "<strong>" + arr[i].substr(0, val.length) + "</strong>";
                            b.innerHTML += arr[i].substr(val.length);
                            /*insert a input field that will hold the current array item's value:*/
                            b.innerHTML += "<input type='hidden' value='" + arr[i] + "'>";
                            /*execute a function when someone clicks on the item value (DIV element):*/
                            b.addEventListener("click", function (e) {
                                /*insert the value for the autocomplete text field:*/
                                inp.value = this.getElementsByTagName("input")[0].value;
                                window.text = this.getElementsByTagName("input")[0].value;

                                /*close the list of autocompleted values,
                                 (or any other open lists of autocompleted values:*/
                                closeAllLists();
                            });
                            a.appendChild(b);
                        }
                    }
                });
                /*execute a function presses a key on the keyboard:*/
                inp.addEventListener("keydown", function (e) {
                    var x = document.getElementById(this.id + "autocomplete-list");
                    if (x)
                        x = x.getElementsByTagName("div");
                    if (e.keyCode == 40) {
                        /*If the arrow DOWN key is pressed,
                         increase the currentFocus variable:*/
                        currentFocus++;
                        /*and and make the current item more visible:*/
                        addActive(x);
                    } else if (e.keyCode == 38) { //up
                        /*If the arrow UP key is pressed,
                         decrease the currentFocus variable:*/
                        currentFocus--;
                        /*and and make the current item more visible:*/
                        addActive(x);
                    } else if (e.keyCode == 13) {
                        /*If the ENTER key is pressed, prevent the form from being submitted,*/
                        e.preventDefault();
                        if (currentFocus > -1) {
                            /*and simulate a click on the "active" item:*/
                            if (x)
                                x[currentFocus].click();
                        }
                    }
                });
                function addActive(x) {
                    /*a function to classify an item as "active":*/
                    if (!x)
                        return false;
                    /*start by removing the "active" class on all items:*/
                    removeActive(x);
                    if (currentFocus >= x.length)
                        currentFocus = 0;
                    if (currentFocus < 0)
                        currentFocus = (x.length - 1);
                    /*add class "autocomplete-active":*/
                    x[currentFocus].classList.add("autocomplete-active");
                }
                function removeActive(x) {
                    /*a function to remove the "active" class from all autocomplete items:*/
                    for (var i = 0; i < x.length; i++) {
                        x[i].classList.remove("autocomplete-active");
                    }
                }
                function closeAllLists(elmnt) {
                    /*close all autocomplete lists in the document,
                     except the one passed as an argument:*/
                    var x = document.getElementsByClassName("autocomplete-items");
                    for (var i = 0; i < x.length; i++) {
                        if (elmnt != x[i] && elmnt != inp) {
                            x[i].parentNode.removeChild(x[i]);
                        }
                    }
                }
                button.addEventListener('click', updateButton);

                function updateButton() {
                    window.valueOfTextField = window.termUrls.get(window.text);
                    if (button.value === 'FindAnswer') {
			    //button.value = 'Answer';window.testText
		     document.getElementById("answerTextBox").value =window.testText ; 
		      //document.getElementById("answerTextBox").value = window.valueOfTextField;
                    } /*else {
                     button.value = 'Start machine';
                     paragraph.textContent = 'The machine is stopped.';
                     }*/
                }

                /*document.getElementById("form_id").addEventListener("submit", myFunction);
                 function myFunction() {
                 window.valueOfTextField = window.termUrls.get(window.text);
                 if (document.getElementById("myInput").value == "") {
                 alert("search box is empty")
                 } else {
                 //document.getElementById('form_id').action = window.location.href = window.valueOfTextField; //Will set it
                 //document.getElementById("output").innerHTML = window.valueOfTextField;
                 document.getElementById("output").value = 'Soap';
                 }
                 }*/


            }

        </script>


    </body>
</html>

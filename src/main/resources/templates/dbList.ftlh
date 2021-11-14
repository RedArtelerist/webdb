<#import "common.ftlh" as c>

<@c.page>

    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-6" style="margin: auto">
                    <div id="alert" style="display: none" class="alert" role="alert"></div>
                    <div class="card" style="padding: 10px">

                       <#-- <div class="header">
                            <div class="form-group mt-3 w-50">
                                <form method="post">
                                    <div class="input-group mb-3">
                                        <input type="text" class="form-control ${(nameError??)?string('is-invalid', '')}"
                                               value="<#if base??>${base.name}</#if>"
                                               name="name" placeholder="Enter name" />

                                        <#if nameError??>
                                            <div class="invalid-feedback">
                                                ${nameError}
                                            </div>
                                        </#if>
                                    </div>

                                    <div class="form-group">
                                        <button type="submit" class="btn btn-primary">Add base</button>
                                    </div>
                                </form>
                            </div>
                        </div>-->

                        <div class="header">
                            <div class="form-group mt-3 w-50">
                                <form id="add" action="#">
                                    <div id="msg"></div>
                                    <div class="input-group">
                                        <input type="text" class="form-control" name="name" placeholder="Enter name" />
                                    </div>

                                    <div id="nameError" class="invalid-feedback"></div>

                                    <div class="form-group" style="margin-top: 5px">
                                        <button id="addBase" class="btn btn-primary">Add base</button>
                                    </div>
                                </form>
                            </div>
                        </div>

                        <div class="content table-responsive table-full-width">
                            <table id="table" class="table table-striped">
                                <thead>
                                    <th>Name</th>
                                    <th></th>
                                    <th></th>
                                </thead>
                                <tbody>
                                <#list bases as base>
                                    <tr id="row_${base.id}">
                                        <td>${base.name}</td>
                                        <td><a href="/base/${base.name}/tables/" class="btn btn-primary">View</a></td>
                                        <td>
                                            <button data-id="${base.id}" data-name="${base.name}" class="delete btn btn-danger">Delete</button>
                                        </td>
                                    </tr>
                                </#list>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script>
        $(document).delegate('#addBase', 'click', function(event) {
            event.preventDefault();
            var namefield = $("#add [name='name']");

            $.ajax({
                type: "POST",
                contentType: "application/json; charset=utf-8",
                url: "http://localhost:8088/database/create",
                data: JSON.stringify({'name': namefield.val()}),
                cache: false,
                success: function(data, status) {
                    console.log(data);

                    $("#table tbody").append("" +
                        "<tr id='row_"+data.id+"'>" +
                        "<td>"+data.name+"</td>" +
                        "<td><a href='/base/"+data.name+"/tables/' class='btn btn-primary'>View</a></td>" +
                        "<td><button data-id='"+data.id+"' data-name='"+data.name+"' class='delete btn btn-danger'>Delete</button></td>" +
                        "</tr>"
                    );

                    clear();
                    showAlert("Base added successfully!", "alert-success");
                },
                error: function(xhr, textStatus) {
                    namefield.addClass("is-invalid");
                    $("#nameError").text(xhr.responseJSON["nameError"]).css("display", "block");
                }
            });
        });

        $(document).on('click', '.delete', function() {
            var row_id = $(this).data('id');
            var name = $(this).data('name');

            $.ajax({
                type: "POST",
                url: "http://localhost:8088/database/" + name + "/delete",
                cache: false,
                success: function(data, status) {
                    $("#row_"+row_id).remove();
                    showAlert(data, "alert-success");
                },
                error: function(xhr, textStatus) {
                    console.log(xhr.responseText)
                    showAlert(xhr.responseText, "alert-danger");
                }
            });
        });

        function showAlert(msg, type){
            $("#alert").addClass(type).text(msg).fadeTo(2000, 500).slideUp(500, function(){
                $("#alert").slideUp(500);
            });
        }

        function clear(){
            var namefield = $("#add [name='name']");
            namefield.val('');
            namefield.removeClass("is-invalid");
            $("#nameError").text('').css("display", "none");
        }

    </script>
</@c.page>
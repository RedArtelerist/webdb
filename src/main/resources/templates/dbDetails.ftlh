<#import "common.ftlh" as c>

<@c.page>
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-6" style="margin: auto">
                    <div id="alert" style="display: none" class="alert" role="alert"></div>
                    <div class="card" style="padding: 10px">
                        <div class="header" style="padding-bottom: 8px">
                            <h4 class="title">Tables in base "${base.name}"</h4>

                            <button type="button" class="btn btn-info" data-toggle="modal" data-target="#form">
                                Add table
                            </button>
                            <a href="/" class="btn btn-secondary" style="float: right">Return</a>
                        </div>

                        <div class="content table-responsive table-full-width">
                            <table id="table" class="table table-striped">
                                <thead>
                                <th>Name</th>
                                <th></th>
                                <th></th>
                                </thead>
                                <tbody>
                                    <#list base.tables as table>
                                        <tr id="row_${table.id}">
                                            <td>${table.name}</td>
                                            <td><a href="/base/${base.name}/table/${table.name}/rows" class="btn btn-primary">View</a></td>
                                            <td>
                                                <button data-id="${table.id}" data-name="${table.name}" class="delete btn btn-danger">Delete</button>
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

    <#include "tableForm.ftlh">

    <script>
        $(document).on('click', '#submit', function(event) {
            event.preventDefault();
            var namefield = $("#form form [name='name']");
            var columns = $("input[name='attribute_name']").map(function(){return $(this).val();}).get();
            var types = $("select[name='attribute_type']").map(function(){return $(this).val();}).get();

            var attributes = [];

            for(var i = 0; i < columns.length; i++){
                attributes.push({name: columns[i], type: types[i]});
            }

            $.ajax({
                type: "POST",
                contentType: "application/json; charset=utf-8",
                url: "http://localhost:8088/base/${base.name}/tables/add",
                data: JSON.stringify({
                    'name': namefield.val(),
                    'attributes': attributes,
                }),
                cache: false,
                success: function(data, status) {
                    console.log(data);
                    var dbName = "${base.name}";
                    $("#table tbody").append("" +
                        "<tr id='row_"+data.id+"'>" +
                        "<td>"+data.name+"</td>" +
                        "<td><a href='/base/"+dbName+"/table/"+data.name+"/rows/' class='btn btn-primary'>View</a></td>" +
                        "<td><button data-id='"+data.id+"' data-name='"+data.name+"' class='delete btn btn-danger'>Delete</button></td>" +
                        "</tr>"
                    );

                    clear();
                    $(".close").click();
                    showAlert("Table added successfully!", "alert-success");
                },
                error: function(xhr, textStatus) {
                    namefield.removeClass("is-invalid");
                    $("#nameError").text('').css("display", "none");

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
                url: "http://localhost:8088/base/${base.name}/tables/" + name + "/remove",
                cache: false,
                success: function(data, status) {
                    $("#row_"+row_id).remove();
                    showAlert(data, "alert-success");
                },
                error: function(xhr, textStatus) {
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
            var namefield = $("#form form [name='name']");
            namefield.val('');
            namefield.removeClass("is-invalid");
            $("#nameError").text('').css("display", "none");
        }
    </script>

</@c.page>
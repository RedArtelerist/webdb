<#import "common.ftlh" as c>

<@c.page>
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12" style="margin: auto">
                    <div id="alert" style="display: none" class="alert" role="alert"></div>
                    <div class="card" style="padding: 10px">
                        <div class="header" style="padding-bottom: 8px">
                            <h4 class="title">Rows in table "${table.name}"</h4>
                            <div style="display: flex">
                                <#--<a href="/base/${table.base.name}/table/${table.name}/rows/create" class="btn btn-info">Add Row</a>-->
                                <button id="btnModal" type="button" class="btn btn-info" data-toggle="modal" data-target="#form">
                                    Insert row
                                </button>
                                <#--<form method="post" action="/base/${table.base.name}/table/${table.name}/deleteDuplicates" style="margin-left: 10px">
                                    <button type="submit" class="btn btn-warning">Delete duplicates</button>
                                </form>-->
                                <button id="delDuplicates" class="btn btn-warning" style="margin-left: 10px">Delete duplicates</button>
                                <a href="/base/${table.base.name}/tables" class="btn btn-secondary" style="margin-left: 68%">Return</a>
                            </div>
                        </div>

                        <div class="content table-responsive table-full-width">
                            <table id="table" class="table table-striped">
                                <thead>
                                <#list table.attributes as attribute>
                                    <th>${attribute.name}</th>
                                </#list>
                                <th></th>
                                <th></th>
                                </thead>
                                <tbody>
                                <#list table.rows as row>
                                    <tr id="row_${row.id}">
                                        <#list row.values as value>
                                            <td>${value}</td>
                                        </#list>
                                        <#--<td><a href="/base/${table.base.name}/table/${table.name}/rows/${row.id}/edit" class="btn btn-primary">Edit</a></td>-->
                                        <td><button data-id="${row.id}" class="edit btn btn-primary">Edit</button></td>
                                        <td><button data-id="${row.id}" class="delete btn btn-danger">Delete</button></td>
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

    <#include "rowForm.ftlh">

    <script>
        $(document).on('click', '#submit', function(event) {
            event.preventDefault();
            var values = $("input[name='values']").map(function(){return $(this).val();}).get();
            var id = $("input[name='id']").val();

            if(id === '')
                insertRow(values);
            else
                editRow(id, values);
        });

        $(document).on('click', '.edit', function() {
            var row_id = $(this).data('id');
            $("input[name='id']").val(row_id);

            var vals = [];
            $("#row_"+row_id+" td").each(function() {
                vals.push($(this).text());
            });

            var values = vals.slice(0, vals.length - 2);
            var inputs = $('input[name="values"]');

            for(var i = 0; i < values.length; i++)
                inputs[i].value = values[i];

            clearErrors();
            $("#btnModal").click();
        });

        $(document).on('click', '.delete', function() {
            var row_id = $(this).data('id');

            $.ajax({
                type: "POST",
                url: "http://localhost:8088/base/${table.base.name}/table/${table.name}/rows/" + row_id + "/remove",
                cache: false,
                success: function(data, status) {
                    $("#row_"+row_id).remove();
                    showAlert(data, "alert-success");
                },
                error: function(xhr, textStatus) {
                    console.log(xhr.responseText);
                    showAlert(xhr.responseText, "alert-danger");
                }
            });
        });

        $(document).on('click', '#delDuplicates', function() {
            $.ajax({
                type: "POST",
                url: "http://localhost:8088/base/${table.base.name}/table/${table.name}/deleteDuplicateRows/",
                cache: false,
                success: function(data, status) {
                    for(const id of data)
                        $("#row_"+id).remove();

                    showAlert("Duplicate rows were successfuly deleted", "alert-warning");
                },
                error: function(xhr, textStatus) {
                    showAlert(xhr.responseText, "alert-danger");
                }
            });
        });

        function insertRow(values){
            $.ajax({
                type: "POST",
                contentType: "application/json; charset=utf-8",
                url: "http://localhost:8088/base/${table.base.name}/table/${table.name}/rows/insert",
                data: JSON.stringify({
                    'values': values,
                    'table': {
                        'id': ${table.id}
                    }
                }),
                cache: false,
                success: function(data, status) {
                    console.log(data);
                    $("#table tbody").append("<tr id='row_"+data.id+"'></tr>");
                    for(const val of data.values)
                        $("#row_"+data.id).append("<td>"+val+"</td>");

                    $("#row_"+data.id).append(
                        "<td><button data-id='"+data.id+"' class='edit btn btn-primary'>Edit</button></td>" +
                        "<td><button data-id='"+data.id+"' class='delete btn btn-danger'>Delete</button></td>"
                    );
                    clear();
                    $(".close").click();
                    showAlert("Row added successfully!", "alert-success");
                },
                error: function(xhr, textStatus) {
                    clearErrors();
                    var errors = JSON.parse(xhr.responseText);

                    Object.entries(errors).forEach((entry) => {
                        const [key, value] = entry;
                        $("#"+key).addClass("is-invalid").next().text(value).css("display", "block");
                    });
                }
            });
        }

        function editRow(id, values){
            $.ajax({
                type: "POST",
                contentType: "application/json; charset=utf-8",
                url: "http://localhost:8088/base/${table.base.name}/table/${table.name}/rows/"+id+"/update",
                data: JSON.stringify({'values': values}),
                cache: false,
                success: function(data, status) {
                    var tds = $("#row_"+id+" td");
                    for(var i = 0; i < values.length; i++) {
                        tds[i].innerHTML = values[i];
                    }

                    clear();
                    $(".close").click();
                    showAlert(data, "alert-info");
                },
                error: function(xhr, textStatus) {
                    clearErrors();
                    var errors = JSON.parse(xhr.responseText);

                    Object.entries(errors).forEach((entry) => {
                        const [key, value] = entry;
                        $("#"+key).addClass("is-invalid").next().text(value).css("display", "block");
                    });
                }
            });
        }

        function showAlert(msg, type){
            $("#alert").addClass(type).text(msg).fadeTo(2000, 500).slideUp(500, function(){
                $("#alert").slideUp(500);
            });
        }

        function clear(){
            $("input[name='id']").val('');
            $('input[name="values"]').each(function() {
                $(this).val('');
            });
            clearErrors();
        }

        function clearErrors(){
            $('input[name="values"]').each(function() {
                $(this).removeClass("is-invalid").next().text('').css("display", "none");
            });
        }

        $(document).on('click', '.close', function() {
            clear();
        });
    </script>
</@c.page>
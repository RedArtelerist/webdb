<#import "common.ftlh" as c>

<@c.page>
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-12" style="margin: auto">
                    <div class="card" style="padding: 10px">
                        <div class="header" style="padding-bottom: 8px">
                            <h4 class="title">Rows in table "${table.name}"</h4>
                            <div style="display: flex">
                                <a href="/base/${table.base.name}/table/${table.name}/rows/create" class="btn btn-info">Add Row</a>
                                <form method="post" action="/base/${table.base.name}/table/${table.name}/deleteDuplicates" style="margin-left: 10px">
                                    <button type="submit" class="btn btn-warning">Delete duplicates</button>
                                </form>
                                <a href="/base/${table.base.name}/tables" class="btn btn-secondary" style="margin-left: 70%">Return</a>
                            </div>
                        </div>

                        <div class="content table-responsive table-full-width">
                            <table class="table table-striped">
                                <thead>
                                <#list table.attributes as attribute>
                                    <th>${attribute.name}</th>
                                </#list>
                                <th></th>
                                <th></th>
                                </thead>
                                <tbody>
                                <#list table.rows as row>
                                    <tr>
                                        <#list row.values as value>
                                            <td>${value}</td>
                                        </#list>
                                        <td><a href="/base/${table.base.name}/table/${table.name}/rows/${row.id}/edit" class="btn btn-primary">Edit</a></td>
                                        <td>
                                            <form method="post" action="/base/${table.base.name}/table/${table.name}/rows/${row.id}/delete">
                                                <button type="submit" class="btn btn-danger">Delete</button>
                                            </form>
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
</@c.page>
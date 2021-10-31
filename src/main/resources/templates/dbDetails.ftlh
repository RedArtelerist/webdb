<#import "common.ftlh" as c>

<@c.page>
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-6" style="margin: auto">
                    <div class="card" style="padding: 10px">
                        <div class="header" style="padding-bottom: 8px">
                            <h4 class="title">Tables in base "${base.name}"</h4>
                            <a href="/base/${base.name}/tables/create" class="btn btn-info">Add table</a>
                            <a href="/" class="btn btn-secondary" style="float: right">Return</a>
                        </div>

                        <div class="content table-responsive table-full-width">
                            <table class="table table-striped">
                                <thead>
                                <th>Name</th>
                                <th></th>
                                <th></th>
                                </thead>
                                <tbody>
                                <#list base.tables as table>
                                    <tr>
                                        <td>${table.name}</td>
                                        <td><a href="/base/${base.name}/table/${table.name}/rows" class="btn btn-primary">View</a></td>
                                        <td>
                                            <form method="post" action="/base/${base.name}/tables/${table.name}/delete">
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
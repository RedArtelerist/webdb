<#import "common.ftlh" as c>

<@c.page>
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-6" style="margin: auto">
                    <div class="card" style="padding: 10px">
                        <div class="header">
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
                        </div>


                        <div class="content table-responsive table-full-width">
                            <table class="table table-striped">
                                <thead>
                                    <th>Name</th>
                                    <th></th>
                                    <th></th>
                                </thead>
                                <tbody>
                                <#list bases as base>
                                    <tr>
                                        <td>${base.name}</td>
                                        <td><a href="/base/${base.name}/tables/" class="btn btn-primary">View</a></td>
                                        <td>
                                            <form method="post" action="/base/${base.name}/delete/">
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
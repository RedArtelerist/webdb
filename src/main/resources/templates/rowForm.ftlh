<#import "common.ftlh" as c>

<@c.page>
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-8" style="margin: auto">
                    <div class="card" style="padding: 10px">
                        <div class="header">
                            <h4 class="title"><#if row?? && row.id??>Update<#else>Add</#if> Row</h4>
                        </div>

                        <div class="content">
                            <form method="post">
                                <#list table.attributes as attribute>
                                    <label for="${attribute.name}">${attribute.name}</label>
                                    <div class="input-group mb-3">
                                        <input type="text"
                                               class="form-control col-md-6 ${(errors?? && errors[attribute.name]??)?string('is-invalid', '')}"
                                               id="${attribute.name}" name="values" placeholder="${attribute.type}"
                                               value="<#if row??>${row.values[attribute?index]}</#if>">

                                        <#if errors?? && errors[attribute.name]??>
                                            <div class="invalid-feedback">
                                                ${errors[attribute.name]}
                                            </div>
                                        </#if>
                                    </div>
                                </#list>


                                <input type="hidden" name="table" value="${table.id}">

                                <input type="hidden" name="id" value="<#if row?? && row.id??>${row.id}</#if>" />

                                <button type="submit" class="btn btn-primary">
                                    <#if row?? && row.id??>Update<#else>Add new</#if> row
                                </button>
                                <a href="/base/${table.base.name}/table/${table.name}/rows" class="btn btn-danger" style="float: right">Cancel</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</@c.page>
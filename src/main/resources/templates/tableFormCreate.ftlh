<#import "common.ftlh" as c>
<style type="text/css">
    .form-elements {
        margin-top: 10px;
    }

    #frm-add-data .form-group {
        margin-left: 13px;
    }
</style>
<@c.page>
    <div class="content">
        <div class="container-fluid">
            <div class="row">
                <div class="col-md-8" style="margin: auto">
                    <div class="card" style="padding: 10px">
                        <div class="header">
                            <h4 class="title">Add new Table</h4>
                        </div>

                        <div class="content">
                            <form method="post">

                                <div class="input-group mb-3">
                                    <input type="text" class="form-control col-md-6 ${(nameError??)?string('is-invalid', '')}"
                                           value="<#if table??>${table.name}</#if>"
                                           name="name" placeholder="Name">

                                    <#if nameError??>
                                        <div class="invalid-feedback">
                                            ${nameError}
                                        </div>
                                    </#if>
                                </div>

                                <input type="hidden" name="base" value="${base.id}">
                                <h6>Attributes</h6>
                                <div class="field_wrapper">
                                    <div class="row">
                                        <div class="form-group col">
                                            <input type="text" class="form-control" placeholder="Enter name"
                                                   name="attribute_name">
                                        </div>
                                        <div class="form-group col">
                                            <select name="attribute_type" class="form-control">
                                                <#list types as type>
                                                    <option value="${type}">${type}</option>
                                                </#list>
                                            </select>
                                        </div>
                                        <div class="form-group col">
                                            <a href="javascript:void(0);" class="btn btn-primary add_button" title="Add field">Add More</a>
                                        </div>
                                    </div>
                                </div>

                                <button type="submit" class="btn btn-primary">Add new table</button>
                                <a href="/base/${base.name}/tables" class="btn btn-danger" style="float: right">Cancel</a>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

</@c.page>

<script type="text/javascript">

    $(document).ready(function () {

        var maxField = 10;

        var addButton = $('.add_button');

        var wrapper = $('.field_wrapper');

        var fieldHTML =
            `<div class="form-elements row">
			    <div class="form-group col">
				    <input type="text" class="form-control" placeholder="Enter name" name="attribute_name">
				</div>
				<div class="form-group col">
				    <select name="attribute_type" class="form-control">
                        <#list types as type>
                            <option value="${type}">${type}</option>
                        </#list>
                    </select>
				</div>
				<div class="form-group col">
					 <a href="javascript:void(0);" class="remove_button" title="Add field">Remove</a>
				</div>
			</div>`;

        var x = 1;

        $(addButton).click(function () {
            if (x < maxField) {
                x++;
                $(wrapper).append(fieldHTML);
            }
        });

        $(wrapper).on('click', '.remove_button', function (e) {
            e.preventDefault();
            $(this).parent().closest(".form-elements").remove();
            x--;
        });
    });
</script>
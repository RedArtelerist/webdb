<div class="modal fade" id="form" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header border-bottom-0">
                <h5 class="modal-title" id="exampleModalLabel">Add new table</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="#">
                <div class="modal-body">
                    <div class="input-group mb-3">
                        <input type="text" class="form-control col-md-6" name="name" placeholder="Name">

                        <div id="nameError" class="invalid-feedback"></div>
                    </div>

                    <h6>Attributes</h6>

                    <div class="field_wrapper">
                        <div class="row">
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
                                <a href="javascript:void(0);" class="btn btn-primary add_button" title="Add field">Add More</a>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer border-top-0 d-flex justify-content-center">
                        <button id="submit" class="btn btn-success">Submit</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script type="text/javascript">

    $(document).ready(function () {
        var maxField = 5;
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
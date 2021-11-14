<div class="modal fade" id="form" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header border-bottom-0">
                <h5 class="modal-title" id="exampleModalLabel">Insert new row</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form action="#">
                <input type="hidden" name="id">

                <div class="modal-body">
                    <#list table.attributes as attribute>
                        <label for="${attribute.name}">${attribute.name}</label>
                        <div class="input-group mb-3">
                            <input type="text" class="form-control col-md-9" id="${attribute.name}"
                                   name="values" placeholder="${attribute.type}">

                            <div class="invalid-feedback"></div>
                        </div>
                    </#list>


                    <div class="modal-footer border-top-0 d-flex justify-content-center">
                        <button id="submit" class="btn btn-success">Submit</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

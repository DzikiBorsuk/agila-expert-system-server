mainModule.controller("homeController", ['$rootScope', '$scope', '$http', '$location', '$window', '$state', 'agilaFactory', 'blockUI', '$uibModal', function ($rootScope, $scope, $http, $location, $window, $state, agilaFactory, blockUI, $uibModal) {

    $rootScope.user = {};
    $rootScope.user.firstName = "Hello";
    $scope.removeEntity = [];

    $http.get('api/user').then(function (response) {
        $rootScope.user.firstName = response.data.firstName;
        $rootScope.user.lastName = response.data.lastName;
    });

    let showAddClass = function(referenceClass, callback) {
        $uibModal.open({
            templateUrl: '../views/home/modalAddClass.html',
            controller: 'addClassController',
            windowClass: 'center-custom-modal',
            backdropFade: true,
            resolve: {
                referenceClass: function () {
                    return referenceClass;
                },
                callback: function () {
                    return callback;
                }
            }
        });
        setTimeout(function () {
            $('.center-custom-modal .modal-dialog').addClass('modal-dialog-centered')
        }, 20);
    };

    let showAddObjectProperty = function(objectProperty, callback) {
        $uibModal.open({
            templateUrl: '../views/home/modalAddObjectProperty.html',
            controller: 'addObjectPropertyController',
            windowClass: 'center-custom-modal',
            backdropFade: true,
            resolve: {
                referenceProperty: function () {
                    return objectProperty;
                },
                callback: function () {
                    return callback;
                }
            }
        });
        setTimeout(function () {
            $('.center-custom-modal .modal-dialog').addClass('modal-dialog-centered')
        }, 20);
    };

    let showAddDataProperty = function(dataProperty, callback) {
        $uibModal.open({
            templateUrl: '../views/home/modalAddDataProperty.html',
            controller: 'addDataPropertyController',
            windowClass: 'center-custom-modal',
            backdropFade: true,
            resolve: {
                referenceProperty: function () {
                    return dataProperty;
                },
                callback: function () {
                    return callback;
                }
            }
        });
        setTimeout(function () {
            $('.center-custom-modal .modal-dialog').addClass('modal-dialog-centered')
        }, 20);
    };

    let deleteEntity = function ($uibModalInstance) {
        blockUI.start();
        $uibModalInstance.close('true');
        $http.delete('api/' + $scope.removeEntity[2] + '/' + $scope.removeEntity[0]).then(function () {
            blockUI.stop();
            agilaFactory.showSuccess('Entity successfully removed');
            load('#' + $scope.removeEntity[3]);

        }, function (response) {
            blockUI.stop();
            agilaFactory.handleRestError(response);
            load('#'+$scope.removeEntity[3]);
        });
    };

    let addClass = function ($uibModalInstance, data) {
        let json = {};
        json.name = data.name;
        json.description=data.description;
        if (data.option === 'child') {
            json.parentName = $scope.currentNode.text;
        }
        else if (data.option === 'sibling') {
            json.parentName = $scope.parentNode;
        }

        $http.post('/api/class',json).then(function () {
            $uibModalInstance.close(true);
            agilaFactory.showSuccess(json.name +' was added successfully!');
            load('#'+$scope.currentTree.replace('-tree',''));
        },function (response) {
            agilaFactory.handleRestError(response);
        });
    };

    let addObjectProperty = function ($uibModalInstance, data) {
        let json = {
            name: data.name,
            description: data.description,
            range: data.range
        };

        if (data.option === 'child') {
            json.parentName = $scope.currentNode.text;
        } else if (data.option === 'sibling') {
            json.parentName = $scope.parentNode;
        }

        $http.post('/api/object-property',json).then(function () {
            $uibModalInstance.close(true);
            agilaFactory.showSuccess(json.name +' was added successfully!');
            load('#'+$scope.currentTree.replace('-tree',''));
        },function (response) {
            agilaFactory.handleRestError(response);
        });
    };

    let addDataProperty = function ($uibModalInstance, data) {
        let json = data;

        if (data.option === 'child') {
            json.parentName = $scope.currentNode.text;
        } else if (data.option === 'sibling') {
            json.parentName = $scope.parentNode;
        }

        $http.post('/api/data-property',json).then(function () {
            $uibModalInstance.close(true);
            agilaFactory.showSuccess(json.name +' was added successfully!');
            load('#'+$scope.currentTree.replace('-tree',''));
        },function (response) {
            agilaFactory.handleRestError(response);
        });
    };

    function load(element) {
        let pattern = 'mx-auto loader';
        element = element + '-tree';
        $(element).empty();
        $(element).addClass(pattern);
        switch (element) {
            case '#class-hierarchy-tree':
                $http.get('/api/class-hierarchy').then(function (response) {
                    $scope.classHierarchy = response.data;
                    $('#class-hierarchy-tree').treeview({
                        data: response.data,
                        levels: 2,
                        collapseIcon: "fa fa-minus-circle",
                        expandIcon: "fa fa-plus-circle",
                        selectable: false
                    });
                    $(element).removeClass(pattern);
                });
                break;

            case '#object-properties-tree':
                $http.get('/api/object-property').then(function (response) {
                    $scope.classHierarchy = response.data;

                    //workaround
                    let json = response.data;
                    json = JSON.parse(JSON.stringify(json).split('"name":').join('"text":'));//workaround
                    json = JSON.parse(JSON.stringify(json).split('"subProperties":').join('"nodes":'));//workaround

                    $('#object-properties-tree').treeview({
                        data: json,
                        collapseIcon: "fa fa-minus-circle",
                        expandIcon: "fa fa-plus-circle",
                        selectable: false
                    });
                    $(element).removeClass(pattern);
                });
                break;

            case '#data-properties-tree':
                $http.get('/api/data-property').then(function (response) {
                    $scope.classHierarchy = response.data;

                    let json = response.data;
                    json = JSON.parse(JSON.stringify(json).split('"name":').join('"text":'));//workaround
                    json = JSON.parse(JSON.stringify(json).split('"subProperties":').join('"nodes":'));//workaround

                    $('#data-properties-tree').treeview({
                        data: json,
                        collapseIcon: "fa fa-minus-circle",
                        expandIcon: "fa fa-plus-circle"
                    });
                    $(element).removeClass(pattern);
                });
                break;

            case '#individuals-tree':
                $http.get('/api/individual').then(function (response) {
                    $scope.classHierarchy = response.data;

                    //workaround
                    let json = response.data;
                    json = JSON.parse(JSON.stringify(json).split('"name":').join('"text":')); //workaround

                    $('#individuals-tree').treeview({
                        data: json,
                        selectable: true,
                    });
                    $(element).removeClass(pattern);
                });
                break;
        }
    }

    function hide() {
        let elements = $('#side-menu').find('a');

        for (let elem of elements) {
            elem.classList.remove("bg-light", "text-dark", "active");
        }

        elements = $('#side-navbar').find("div");

        for (let elem of elements) {
            elem.classList.remove("navbar-custom-show");
        }
    }

    $(document).ready(function () {

        $('body').on('click', function () {
            $(".popover").popover('hide');
        });

        $('.movable').on('click', function () {

            let element = $(this).attr('id');
            element = "#" + element;

            if ($(element).hasClass("active")) {
                hide();
                $('#main').removeClass("main-collapsed");
            }
            else {
                hide();
                $(element).addClass("bg-light text-dark active");
                element = element.replace("-btn", "");
                $(element).addClass('navbar-custom-show');
                $('#main').addClass('main-collapsed');
                load(element);
            }

        });

        $('#logout-btn').on('click', function () {
            $http.post('/logout', {}).then(function () {
                $rootScope.authenticated = false;
                $window.location.reload();
            }, function (data) {
                $rootScope.authenticated = false;
            });
        });

        $('#side-navbar').on('mouseenter mouseleave',
            '.node-class-hierarchy-tree, .node-object-properties-tree, .node-data-properties-tree', function (event) {
            let element = $(this);

            if (event.type === 'mouseenter') {
                element.append('<div class="hover-span-edit float-right text-muted">' +
                    '<span alt="Add child/sibling" class="fa fa-sitemap mr-2 hover-span-edit-icon hover-span-edit-add"></span>' +
                    '<span alt="Remove"  class="fa fa-trash mr-2 hover-span-edit-icon hover-span-edit-remove"></span>' +
                    '</div>');
            }
            else if (event.type === 'mouseleave') {
                element.find('.hover-span-edit').remove();
            }
        });

        $('#side-navbar').on('mouseenter mouseleave', '.node-individuals-tree', function (event) {
            let element = $(this);

            if (event.type === 'mouseenter') {
                element.append('<div class="hover-span-edit float-right text-muted">' +
                    '<span alt="Remove"  class="fa fa-trash mr-2 hover-span-edit-icon hover-span-edit-remove"></span>' +
                    '</div>');
            }
            else if (event.type === 'mouseleave') {
                element.find('.hover-span-edit').remove();
            }
        });


        $('#side-navbar').on('nodeSelected', '.ontology-tree', function (event) {
            let selected = $(this).treeview('getSelected');

            $scope.currentNode = selected[0];
            let parent = $(this).treeview('getParents', selected);
            if (parent.length > 0) {
                $scope.parentNode = parent[0].text;
            } else {
                $scope.parentNode = null;
            }
            $scope.currentTree = $(this).attr('id');

            if ($scope.currentTree == "individuals-tree") {
                $state.go("individualDetails", {
                    id: $scope.currentNode.text
                });
            } else if ($scope.currentTree == "data-properties-tree") {

                blockUI.start();

                let element = $(this).treeview('getSelected')[0].$el;
                $http.get('api/data-property/' + $scope.currentNode.text).then(function (response) {
                    element.popover({
                        html: true,
                        content: "<div class='text-center'><p class='mt-2'><small class='alert alert-info m-0 p-2'>" +  response.data.dataType + "</small></p>" + (response.data.description != null ? ("<p>" + response.data.description + "</p></div>") : "</div>") ,
                        trigger: 'focus',
                        container: $('#main'),
                    });
                    element.popover('show');
                    blockUI.stop();
                });

            } else if ($scope.currentTree == "object-properties-tree") {

                blockUI.start();

                let element = $(this).treeview('getSelected')[0].$el;
                $http.get('api/object-property/' + $scope.currentNode.text).then(function (response) {
                    element.popover({
                        html: true,
                        content: "<div class='text-center'><p class='mt-2'><small class='alert alert-info m-0 p-2'>" +  response.data.range + "</small></p>" + (response.data.description != null ? ("<p>" + response.data.description + "</p></div>") : "</div>") ,
                        trigger: 'focus',
                        container: $('#main'),
                    });
                    element.popover('show');
                    blockUI.stop();
                });

            } else {
                $(this).treeview('unselectNode', [selected, {silent: true}]);
            }
        });


        $('#side-navbar').on('click', '.hover-span-edit-icon', function (event) {
            event.stopImmediatePropagation();
            let element = $(this);
            let element_data = [];
            let name = element.parent().parent()
                .clone()    //clone the element
                .children() //select all the children
                .remove()   //remove all the children
                .end()  //again go back to selected element
                .text();
            element_data.push(name);

            if (element.closest('#class-hierarchy-tree').length > 0) {
                element_data.push('class-hierarchy');
            }
            else if (element.closest('#object-properties-tree').length > 0) {
                element_data.push('object-properties');
            }
            else if (element.closest('#data-properties-tree').length > 0) {
                element_data.push('data-properties');
            }
            else if (element.closest('#individuals-tree').length > 0) {
                element_data.push('individuals');
            }

            if (element.hasClass('hover-span-edit-add')) {
                switch (element_data[1]) {
                    case 'class-hierarchy':
                        $scope.addClass = {};
                        $scope.addClass.className = element_data[0];
                        showAddClass($scope.addClass.className, addClass);
                        break;
                    case 'object-properties':
                        $scope.objectProperty = {
                            name: $scope.currentNode.text,
                            range: $scope.currentNode.range
                        };
                        showAddObjectProperty($scope.objectProperty, addObjectProperty);
                        break;
                    case 'data-properties':
                        $scope.dataProperty = {
                            name: $scope.currentNode.text,
                            range: $scope.currentNode.range
                        };
                        showAddDataProperty($scope.dataProperty, addDataProperty);
                        break;
                    case 'individuals':

                        break;
                }
            }
            else if (element.hasClass('hover-span-edit-edit')) {
                switch (element_data[1]) {
                    case 'class-hierarchy':
                        break;
                    case 'object-properties':
                        break;
                    case 'data-properties':
                        break;
                    case 'individuals':
                        break;
                }
            }
            else if (element.hasClass('hover-span-edit-remove')) {
                if (element_data[0] !== 'Thing') {
                    $scope.removeEntity = [];
                    $scope.removeEntity.push(element_data[0]);
                    switch (element_data[1]) {
                        case 'class-hierarchy':
                            $scope.removeEntity.push('Class Hierarchy');
                            $scope.removeEntity.push('class');
                            break;
                        case 'object-properties':
                            $scope.removeEntity.push('Object Properties');
                            $scope.removeEntity.push('object-property');
                            break;
                        case 'data-properties':
                            $scope.removeEntity.push('Data Properties');
                            $scope.removeEntity.push('data-property');
                            break;
                        case 'individuals':
                            $scope.removeEntity.push('Cars');
                            $scope.removeEntity.push('individual');
                            break;
                    }
                    $scope.removeEntity.push(element_data[1]);
                    let title = '<span>Delete</span>';
                    let message = 'Do you want to delete <span class="font-weight-bold">'+$scope.removeEntity[0]+'</span> from <span class="font-weight-bold">'+$scope.removeEntity[1]+'</span>';
                    agilaFactory.showAlert(title,message,'btn-secondary','btn-danger','Delete',deleteEntity);
                }
            }
        });
    });
}]);
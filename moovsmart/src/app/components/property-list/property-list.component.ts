import { applySourceSpanToExpressionIfNeeded } from '@angular/compiler/src/output/output_ast';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { FilteredListModel } from '../../models/FilteredListModel';
import { PropertyListItemModel } from '../../models/propertyListItem.model';
import { PropertyService } from '../../services/property.service';


@Component({
    selector: 'app-property-list',
    templateUrl: './property-list.component.html',
    styleUrls: ['./property-list.component.css'],
})
export class PropertyListComponent implements OnInit {


    propertyListItemModels: Array<PropertyListItemModel>;
    defaultPicture = 'https://atasouthport.com/wp-content/uploads/2017/04/default-image.jpg';

    actualPageList: [PropertyListItemModel[]];
    actualPageNumber: number;

    propertyTypes: PropertyTypeOptionModel[];
    propertyStates: PropertyStateOptionModel[];
    cities: string[];

    id: string;
    registratedUser: boolean;
    filteredForm: FormGroup;
    filteredFormDatas: any;
    needFilterList: boolean;

    ngOnInit() {

        if (localStorage.getItem('user') != null) {
            this.registratedUser = true;
        }
        this.filterOpenMessage = 'Szűrni szeretnék';
        this.clearFilterFields();


        this.propertyService.getPropertyList().subscribe(
            propertyListItems => {
                this.propertyListItemModels = propertyListItems;
                this.actualPageList = this.makingActualList(this.propertyListItemModels);
                console.log(this.actualPageList);
            },
        );

    }

    private makingActualList(propertyListItemModels: Array<PropertyListItemModel>) {
        const listSize = propertyListItemModels.length;
        const miniListSize = 4;
        let actualList: [PropertyListItemModel[]] = [[]];
        let tempList: PropertyListItemModel[] = [];

        let indexBig = 0;
        for (let i = 0; i < listSize; i++) {
            let indexMini = 1;
            const property = propertyListItemModels[i];
            tempList.push(property);

            if (i!== 0 && ((i+1) % miniListSize) === 0) {
                actualList.push(tempList);
                indexBig++;
                tempList = [];
                indexMini = 0;
            }
            indexMini++;
        }
        actualList.push(tempList);
        actualList.shift();
        return actualList;
    }

    filterOpenMessage: string;

    constructor(private propertyService: PropertyService,
                private router: Router,
                private route: ActivatedRoute) {
        this.filteredForm = new FormGroup(
            {
                'minPrice': new FormControl(0),
                'maxPrice': new FormControl(null),
                'minSize': new FormControl(0),
                'maxSize': new FormControl(null),
                'propertyState': new FormControl(null),
                'propertyType': new FormControl(null),
                'city': new FormControl(null),
                'numberOfRooms': new FormControl(2),
            },
        );
        this.needFilterList = false;

    }

    clearFilterFields() {
        this.filteredForm = new FormGroup(
            {
                'minPrice': new FormControl(0),
                'maxPrice': new FormControl(null),
                'minSize': new FormControl(0),
                'maxSize': new FormControl(null),
                'propertyState': new FormControl(null),
                'propertyType': new FormControl(null),
                'city': new FormControl(null),
                'numberOfRooms': new FormControl(2),
            },
        );
    }

    details(id: number) {
        this.router.navigate(['property-details', id]);
    }

    filterProperties() {
        this.filteredFormDatas = this.filteredForm.value;
        this.sendFilterFieldList(this.filteredFormDatas);
    }

    private sendFilterFieldList(filteredFormDatas: FilteredListModel) {
        this.propertyService.sendFilterList(filteredFormDatas).subscribe(
            (filteredProperties) => this.propertyListItemModels = filteredProperties,
        );
    }

    makeFilterBar() {
        if (this.needFilterList === false) {
            this.needFilterList = true;
            this.filterOpenMessage = 'Szűrés kikapcsolása';
            this.propertyService.getInitialFormData().subscribe((formInitData: FormInitDataModel) => {
                this.propertyTypes = formInitData.propertyTypes;
                this.propertyStates = formInitData.propertyStates;
            });

            this.propertyService.getCityList().subscribe(
                (data: string[]) => {
                    this.cities = data;
                    console.log(this.cities);
                },
            );

        } else {
            this.needFilterList = false;
            this.filterOpenMessage = 'Szűrni szeretnék';
        }

    }


}

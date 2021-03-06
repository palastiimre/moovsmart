import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { environment } from '../../environments/environment';
import { DateIntervalModel } from '../models/DateInterval.model';
import { FilteredListModel } from '../models/FilteredListModel';
import { PropertyDetailsModel } from '../models/propertyDetails.model';
import { PropertyFormDataModel } from '../models/propertyFormData.model';
import { PropertyListByAdmin } from '../models/propertyListByAdmin.model';
import { PropertyListItemModel } from '../models/propertyListItem.model';
import { UserDetailsModel } from '../models/userDetails.model';
import { UserFormDataModel } from '../models/userFormData.model';

@Injectable({
    providedIn: 'root',
})
export class PropertyService {

    userName = new Subject<string>();
    userId: number;
    regisTrated = new Subject<boolean>();

    baseUrl = environment.apiUrl + 'api/properties';
    baseUserUrl = environment.apiUrl + 'api/user';
    baseImageUrl = environment.apiUrl + 'api/images';

    url = (environment.apiUrl);

    constructor(private httpClient: HttpClient) {
    }

    getInitialFormData(): Observable<FormInitDataModel> {
        return this.httpClient.get<FormInitDataModel>(this.baseUrl + '/formData');
    }

    createProperty(roomFormData: PropertyFormDataModel): Observable<any> {
        return this.httpClient.post(this.baseUrl + '/authUser', roomFormData);
    }

    getPropertyList(): Observable<Array<PropertyListItemModel>> {
        return this.httpClient.get<Array<PropertyListItemModel>>(this.baseUrl);
    }

    getPropertyDetails = (id: number): Observable<PropertyDetailsModel> => {
        return this.httpClient.get<PropertyDetailsModel>(this.baseUrl + '/' + id);
    };


    updateProperty(data: PropertyFormDataModel, propertyId: number): Observable<any> {
        data.id = propertyId;
        return this.httpClient.put<any>(this.baseUrl + '/authUser/' + propertyId, data);
    }

    deleteProperty(id: number): Observable<any> {
        return this.httpClient.delete<any>(this.baseUrl + '/authUser/' + id);
    }

    fetchPropertyData(id: string): Observable<PropertyFormDataModel> {
        return this.httpClient.get<PropertyFormDataModel>(this.baseUrl + '/authUser/' + id);
    }

    registerUser(userFormData: UserFormDataModel): Observable<any> {
        return this.httpClient.post(this.baseUserUrl + '/registration', userFormData);
    }

    validateUser(id: string): Observable<any> {
        return this.httpClient.get<any>(this.baseUserUrl + '/validuser/' + id);
    }

    signIn(credentials: any): Observable<any> {
        const headers = new HttpHeaders(credentials ? {
            authorization: 'Basic ' + btoa(credentials.userName + ':' + credentials.password),
        } : {});
        return this.httpClient.get<any>(this.baseUserUrl + '/me', {headers: headers});
    }

    getMyPropertyList(): Observable<Array<PropertyListItemModel>> {
        return this.httpClient.get<Array<PropertyListItemModel>>(this.baseUrl + '/authUser/myList');
    }

    getMyHoldingPropertyList(): Observable<Array<PropertyListByAdmin>> {
        return this.httpClient.get<Array<PropertyListByAdmin>>(this.baseUrl + '/authUser/myHoldingList');
    }

    getCityList(): Observable<string[]> {
        return this.httpClient.get<string[]>(this.baseUrl + '/getCityList');
    }

    sendFilterList(datas: FilteredListModel): Observable<Array<PropertyListItemModel>> {
        return this.httpClient.post<Array<PropertyListItemModel>>(this.baseUrl + '/filteredList', datas);
    }

    getPictures(idParam: number): Observable<PictureListItemModel> {
        return this.httpClient.get<PictureListItemModel>(this.baseUrl + '/' + idParam + '/images');
    }

    deletePicture(pictureIdToDelete: string, propertyId: number): Observable<any> {
        return this.httpClient.post<any>(this.baseUrl + '/' + propertyId + '/images', pictureIdToDelete);
    }

    updatePictureList(imageList: PictureListItemModel, propertyId: number): Observable<any> {
        return this.httpClient.post<any>(this.baseUrl + '/images/' + propertyId, imageList);
    }

    getPropertyListForApproval(): Observable<any> {
        return this.httpClient.get<Array<PropertyListByAdmin>>(this.baseUrl + '/admin/propertyListForApproval');
    }

    getPropertyDetailsForApproval = (idParam: number): Observable<PropertyFormDataModel> => {
        return this.httpClient.get<PropertyFormDataModel>(this.baseUrl + '/admin/propertyDetailsForApproval/' + idParam);
    };

    setPropertyToAccepted(id: number): Observable<any> {
        return this.httpClient.put<any>(this.baseUrl + '/admin/activateProperty/' +id, id);

    }

    setPropertyToForbidden(propertyToDelete: number): Observable<any> {
        return this.httpClient.put<any>(this.baseUrl + '/admin/forbiddenProperty/' +propertyToDelete
            , propertyToDelete);
    }

    getArchivedProperties(formData: DateIntervalModel): Observable<any> {
        return this.httpClient.post<PropertyFormDataModel>(this.baseUrl
            + '/admin/getArchivedProperties', formData);
    }

    getUserByMail(formData: string): Observable<any> {
        return this.httpClient.get<UserDetailsModel>(this.baseUserUrl
            + '/getUserByUserMail/' + formData);
    }

    banUser(userIdForBan: number): Observable<any> {
        return this.httpClient.put(this.baseUserUrl + '/admin/banUser/' + userIdForBan,
            userIdForBan);
    }

    unBanUser(id: number) {
        return this.httpClient.put(this.baseUserUrl + '/admin/permitUser/' + id, id);
    }

    reactivateProperty(id: number) {
        return this.httpClient.put(this.baseUrl + '/admin/activateProperty/' + id, id);
    }

    refreshArchivedPropertyList(): Observable<any> {
        return this.httpClient.get<Array<PropertyListItemModel>>(this.baseUrl +
        '/admin/getArchivedProperties');
    }

    getPropertyListByMail(mail: string): Observable<any> {
        return this.httpClient.get<Array<PropertyListItemModel>>(this.baseUrl
            + '/admin/getPropertyListByUserMail/' + mail);
    }
    public uploadImage(image: File): Observable<any> {
        const uploadData = new FormData();
        uploadData.append('myPicture', image);
        console.log(this.baseUrl + " service url");
        const imageServiceReturn = this.httpClient.post(this.baseImageUrl, uploadData);
        return imageServiceReturn;
    }

    logout() {
        console.log(this.url);
        return this.httpClient.post(this.url + 'logout', {});
    }
}

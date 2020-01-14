import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { NgModule } from '@angular/core';
import { ReactiveFormsModule } from '@angular/forms';
import { BrowserModule } from '@angular/platform-browser';
import { CloudinaryModule } from '@cloudinary/angular-5.x';
import * as  Cloudinary from 'cloudinary-core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { PropertyDetailsComponent } from './components/property-details/property-details.component';
import { PropertyFormComponent } from './components/property-form/property-form.component';
import { PropertyListComponent } from './components/property-list/property-list.component';
import { RegistrationComponent } from './components/registration/registration.component';
import { SigninComponent } from './components/signin/signin.component';
import { WelcomeComponent } from './components/welcome/welcome.component';
import { HttpRequestInterceptor } from './utils/httpRequestInterceptor';
import { DeletePictureComponent } from './components/delete-picture/delete-picture.component';

@NgModule({
    declarations: [
        AppComponent,
        NavbarComponent,
        PropertyFormComponent,
        PropertyDetailsComponent,
        PropertyListComponent,
        RegistrationComponent,
        SigninComponent,
        WelcomeComponent,
        DeletePictureComponent,
    ],
    imports: [
        HttpClientModule,
        ReactiveFormsModule,
        BrowserModule,
        AppRoutingModule,
        CloudinaryModule.forRoot(Cloudinary, {cloud_name: 'moovsmart'}),
    ],
    providers: [
        [{provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true}],
    ],
    bootstrap: [AppComponent],
})
export class AppModule {
}

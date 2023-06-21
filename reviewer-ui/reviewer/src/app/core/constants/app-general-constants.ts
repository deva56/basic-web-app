import { environment } from "src/environments/environment"

export class ApplicationGeneralConstants {
    static applicationName: string = "Application name"
    static apiUrlEndpoint = `${environment.apiUrl}/api/v1/`
    static apiUrl: string = environment.apiUrl
    static companyName: string = "Company name"
}
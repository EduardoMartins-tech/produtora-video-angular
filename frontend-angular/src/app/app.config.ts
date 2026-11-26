import { ApplicationConfig, LOCALE_ID, provideBrowserGlobalErrorListeners } from '@angular/core';
import { provideClientHydration, withEventReplay } from '@angular/platform-browser';
import { provideHttpClient, withFetch } from '@angular/common/http';
import { registerLocaleData } from '@angular/common';
import localePt from '@angular/common/locales/pt';

// Datas e moeda no formato brasileiro (dd/MM/yyyy e R$ 1.234,56)
registerLocaleData(localePt);

// Configuração principal da aplicação Angular.
export const appConfig: ApplicationConfig = {
  providers: [
    // Tratamento global de erros.
    provideBrowserGlobalErrorListeners(),

    // Hidratação do projeto criado com SSR.
    provideClientHydration(withEventReplay()),

    // Comunicação HTTP com o Spring Boot.
    provideHttpClient(withFetch()),

    { provide: LOCALE_ID, useValue: 'pt-BR' },
  ],
};

import { Injectable } from '@angular/core';
import { MonacoEditorLoaderService } from '@materia-ui/ngx-monaco-editor';

import { filter, take } from 'rxjs';
import { registerGroovyLanguageForMonaco } from './groovy-language-definition-for-monaco';

@Injectable({
  providedIn: 'root'
})
export class ScriptingService {

  constructor(private monacoLoaderService: MonacoEditorLoaderService) {
    this.monacoLoaderService.isMonacoLoaded$.pipe(
      filter(isLoaded => isLoaded),
      take(1),
    ).subscribe(() => {
      registerGroovyLanguageForMonaco(monaco.languages)
    });
  }
}

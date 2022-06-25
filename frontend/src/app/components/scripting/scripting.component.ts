import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MonacoEditorLoaderService } from '@materia-ui/ngx-monaco-editor';

import { filter, take } from 'rxjs';
import { registerGroovyLanguageForMonaco } from './groovy-language-definition-for-monaco';

@Component({
  selector: 'app-scripting',
  templateUrl: './scripting.component.html',
  styleUrls: ['./scripting.component.css']
})
export class ScriptingComponent implements OnInit {

  editorOptions = { theme: 'vs-dark', language: 'groovy' };

  @Input()
  code: string = '';

  @Output()
  codeChange = new EventEmitter();

  change(newCode: string) {
    console.log('newvalue', newCode)
    this.code = newCode;
    this.codeChange.emit(newCode);
  }

  constructor(private monacoLoaderService: MonacoEditorLoaderService) {
    this.monacoLoaderService.isMonacoLoaded$.pipe(
      filter(isLoaded => isLoaded),
      take(1),
    ).subscribe(() => {
      registerGroovyLanguageForMonaco(monaco.languages)
    });
  }

  ngOnInit(): void {
  }

}

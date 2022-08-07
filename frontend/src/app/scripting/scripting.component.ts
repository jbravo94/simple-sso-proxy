import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ScriptingService } from 'src/app/scripting/scripting.service';

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

  private scriptingService: ScriptingService

  change(newCode: string) {
    this.code = newCode;
    this.codeChange.emit(newCode);
  }

  constructor(scriptingService: ScriptingService) {
    // Injection needed because of lazy loading of service which contains editor configs.
    this.scriptingService = scriptingService;
  }

  ngOnInit(): void {
  }

}

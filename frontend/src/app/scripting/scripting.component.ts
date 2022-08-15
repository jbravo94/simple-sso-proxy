/*
The MIT License
Copyright © 2022 Johannes HEINZL

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
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

'use strict';

const React = require('react');
const ReactDOM = require('react-dom')
const client = require('./client');
const FileUpload = require('react-fileupload');
const ReactCSSTransitionGroup = require('react-addons-css-transition-group') // ES5 with npm


class App extends React.Component {

	constructor(props) {
		super(props);
		this.state = {files: []};
	}


	componentDidMount() {
		client({method: 'GET', path: '/upload/uploadFiles'}).done(response => {
			this.setState({files: response.entity._embedded.uploadFiles});
		});
	}

	render() {
		return (
            <div>
		    <FileInputForm parent={this}/>
                  {this.state.files.length > 0 ? <FileList files={this.state.files}/> : null}
            </div>
		)
	}
}

class FileInputForm extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
		    fileSelected: false,
		    errmsg: false,
		    msg: '',
            filename: ''
		}
        this.options={
            parent: props.parent,
            baseUrl:'/files',
            beforeUpload: this.handleBeforeFile,
            uploadError: this.handleError,
            uploadFail: this.handleError,
            uploadSuccess: this.handleSuccess,
            beforeChoose: this.handleBeforeChoose,
            chooseFile: this.handleChoosenFile,
            fileFieldName: 'file',
//            dataType: 'json',
            textBeforeFiles: true,
            requestHeaders: {'accept':'application/json'},
            param: {
                title: this.title,
                description: this.description
            },
        }

	}

  handleBeforeFile(files,mill,xhrID) {
    this.param.title = document.getElementById('title').value
    this.param.description = document.getElementById('description').value
    if(this.param.title === '' || this.param.description === ''){
        this.props.parent.setState({fileSelected: false,
                                    errmsg: true,
                                    msg: 'Title and Description are required',
                                    filename: ''})
        return false
    }
    return true
  }

  handleBeforeChoose(err) {
        this.props.parent.setState({fileSelected: false,
                                   	errmsg: false,
                                   	msg: '',
                                   	filename: ''})
  }

  handleChoosenFile(files){
    if(files.length > 0) {
        this.props.parent.setState({fileSelected: true,
                                   	errmsg: false,
                                   	msg: '',
                                   	filename: files[0].name})
    }
    else {
        this.props.parent.setState({fileSelected: false,
                                    errmsg: false,
                                    msg: 'File Not Selected',
                                    filename: ''})
    }
  }
  handleError(err) {
        this.props.parent.setState({fileSelected: false,
                                   	errmsg: true,
                                   	msg: err.message,
                                   	filename: ''})
  }

  handleSuccess(resp) {
    console.log('response:', resp);
		client({method: 'GET', path: '/upload/uploadFiles'}).done(response => {
			this.props.options.parent.setState({files: response.entity._embedded.uploadFiles});
		});
	document.getElementById('title').value = ''
	document.getElementById('description').value = ''
    this.props.parent.setState({fileSelected: false,
                                    	errmsg: false,
                                    	msg: 'Sucessfully uploaded file:' + this.props.parent.setState.filename,
                                    	filename: ''})
 }

  render() {
    var showmsg = this.state.msg.length > 0 ? "msg-show alert " : "msg-hide alert "
    showmsg += this.state.errmsg ? "alert-danger" :  "alert-success"
    var uploadbutton = this.state.fileSelected ?
       <button className="btn btn-sm btn-primary" ref="uploadBtn">Upload</button> :
         <div className="msg-hide btn" >Upload</div>

    return (
        <div>
        <FileUpload options={this.options} parent={this}>
            <div className="col-md-1"><label className="label label-danger">Title: </label></div>
            <div className="col-md-6"><input id="title" type="text"/></div>
            <div className="div-label col-md-1"><label className="label label-danger">Description: </label></div>
            <div className="col-md-6"><input id="description" type="text"/></div>
            <div className="div-label col-md-1"><label className="label label-danger">File Name: </label></div>
            <div className="col-md-6"><input disabled id="filename" type="text" value={this.state.filename}/></div>
                <button className="div-btn btn btn-sm btn-default" ref="chooseBtn">Choose File</button>
                {uploadbutton}
        </FileUpload>
        <div className={showmsg}>{this.state.msg}</div>
        </div>
    )
  }

}

const colsize = "col-xs-6 col-sm-3"

const FileList = (props) => {
		var files = props.files.map(uploadfile =>
			<UploadedFile key={uploadfile._links.self.href} uploadfile={uploadfile}/>
		)
		return (
		    <div>
			<div className="row">
                <div className={colsize}>Title</div>
                <div className={colsize}>Description</div>
                <div className={colsize}>Date Created</div>
                <div className={colsize}>Download</div>
			</div>
			{files}
			</div>
		)
	}

const UploadedFile = (props) =>{
    return (
        <div className="row">
            <div className={colsize}>{props.uploadfile.title}</div>
            <div className={colsize}>{props.uploadfile.description}</div>
            <div className={colsize}>{props.uploadfile.dateCreated}</div>
            <div className={colsize}><a href={'/files/' + props.uploadfile.originalName}>{props.uploadfile.originalName}</a></div>
        </div>
    )
}

ReactDOM.render(
	<App />,
	document.getElementById('react')
)


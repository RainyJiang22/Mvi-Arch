# Mvi-Arch

Pursue better practice of android architecture

## MVI Feature

- 相对于MVVM，强调是数据单向流动，很容易进行数据比对与回溯
- 使用ViewState对State进行集中管理，只需要订阅一个`ViewState`就可以获取页面所有状态，减少了很多模板代码
- 通过viewState与Action进行通信，浏览`viewstate`与`action`就可以清楚知道`viewModel`的职责
- 如果用MVI代替没有使用`databinding`的MVVM是一个比较好的选择

## 核心架构
<div style="float:right">
  <img src="http://m.qpic.cn/psc?/V526iEgm3HgG9w0K6aQL2X9HJE4OnV96/ruAMsa53pVQWN7FLK88i5uh.esy8dQlWqrURok1A5d1zpBnGz8lmOXdQ7ZghPKPMCc9xABkfkaSoDiUPbxe92dx5pWzpRtfRT4r3xBtQuag!/b&bo=gAK1AYACtQEBFzA!&rf=viewer_4"/>
</div>

## 核心组件
- Model：与MVVM中的Model不同的是，MVI的Model主要指UI状态（State）。例如页面加载状态、控件位置等都是一种UI状态。
- View：  与其他MVX中的View一致，可能是一个Activity或者任意UI承载单元。MVI中的View通过订阅Model的变化实现界面刷新。
- Intent：此Intent不是Activity的Intent，用户的任何操作都被包装成Intent后发送给Model层进行数据请求。
  